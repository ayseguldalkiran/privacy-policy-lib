package com.example.privacy_policy_lib

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.privacy_policy_lib.adapter.ContractsAdapter
import com.example.privacy_policy_lib.core.AgreementTypes
import com.example.privacy_policy_lib.core.model.ContractItem
import com.example.privacy_policy_lib.core.model.PrivacyPolicyLibParams
import com.example.privacy_policy_lib.core.model.PrivacyPolicyState
import com.example.privacy_policy_lib.core.utils.ContextUtils
import com.example.privacy_policy_lib.core.utils.IntentExtraName
import com.example.privacy_policy_lib.databinding.FragmentContractsBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ContractsFragment: Fragment(), ContractsAdapter.OnAllCheckboxCheckedListener {
    private var _binding: FragmentContractsBinding? = null
    private val binding get() = _binding!!
    private var mAdapter: ContractsAdapter? = null
    private val checkBoxViewModel: CheckBoxViewModel by activityViewModels()
    private var privacyPolicyFile: String? = null
    var contractItemList = arrayListOf(ContractItem())
    var onPrivacyPolicyAccepted: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { args ->
            privacyPolicyFile = args.getString(IntentExtraName.ARG_FILE)
            val params = args.getParcelable<PrivacyPolicyLibParams>("params")

            contractItemList = if (params != null) {
                PrivacyPolicyState.params = params
                ArrayList(params.agreementTypes.filter { agreement ->
                    val currentDateTime = LocalDateTime.now()
                    params.agreementTokenList.none { it.first == agreement } ||
                            params.endDateList.any {
                                val endDateTime = LocalDateTime.parse(it.second, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                                currentDateTime.isAfter(endDateTime)
                            }
                }.map { filteredAgreement ->
                    ContractItem(AgreementTypes.getStringForEnum(filteredAgreement, requireContext()))
                })
            } else {
                val defaultAgreements = arrayListOf(
                    AgreementTypes.GENERALAGREEMENT,
                    AgreementTypes.TERMSOFUSE
                )
                ArrayList(defaultAgreements.map { agreement ->
                    ContractItem(AgreementTypes.getStringForEnum(agreement, requireContext()))
                })
            }
        }

        context?.let { ContextUtils.setmContext(it) }
        mAdapter = ContractsAdapter(requireActivity()).apply {
            onContractClicked = { position ->
                val fragment = PrivacyPolicyDialogFragment.newInstance(privacyPolicyFile!!, position)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
        mAdapter!!.addItem(contractItemList)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentContractsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val recyclerView = requireView().findViewById<RecyclerView>(R.id.rcw)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = mAdapter
        binding.btnRead.setOnClickListener {
            onPrivacyPolicyAccepted?.invoke()
            parentFragmentManager.popBackStack()
        }
        checkBoxViewModel.initialize(contractItemList.size)
        checkBoxViewModel.checkboxStates.observe(viewLifecycleOwner) { states ->
            mAdapter?.updateCheckboxStates(states)
            binding.btnRead.setBackgroundColor(
                if (states.all { it }) resources.getColor(R.color.colorPrimary)
                else resources.getColor(R.color.colorDisabled)
            )
        }

        parentFragmentManager.setFragmentResultListener("privacy_policy", viewLifecycleOwner) { _, bundle ->
            val position = bundle.getInt("position")
            checkBoxViewModel.setCheckboxState(position, true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onAllCheckboxChecked(allChecked: Boolean) {
        binding.btnRead.setBackgroundColor(
            if (allChecked) resources.getColor(R.color.colorPrimary)
            else resources.getColor(R.color.colorDisabled)
        )
    }
}