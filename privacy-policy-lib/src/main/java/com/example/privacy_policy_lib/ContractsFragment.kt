package com.example.privacy_policy_lib

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.privacy_policy_lib.adapter.ContractsAdapter
import com.example.privacy_policy_lib.core.model.ContractItem
import com.example.privacy_policy_lib.core.model.PrivacyPolicyState
import com.example.privacy_policy_lib.core.model.PrivacyPolicyState.PARAMS_TO_SEND_TO_APP
import com.example.privacy_policy_lib.core.utils.ContextUtils
import com.example.privacy_policy_lib.core.utils.IntentExtraName
import com.example.privacy_policy_lib.databinding.FragmentContractsBinding


class ContractsFragment: Fragment(), ContractsAdapter.OnAllCheckboxCheckedListener {
    private var _binding: FragmentContractsBinding? = null
    private val binding get() = _binding!!
    private var mAdapter: ContractsAdapter? = null
    private val checkBoxViewModel: CheckBoxViewModel by activityViewModels()
    private var privacyPolicyFile: String? = null
    var contractItemList = arrayListOf(ContractItem())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            privacyPolicyFile = it.getString(IntentExtraName.ARG_FILE)
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
            val resultIntent = Intent().apply {
                putExtra(PARAMS_TO_SEND_TO_APP, PrivacyPolicyState.params)
            }
            requireActivity().setResult(Activity.RESULT_OK, resultIntent)
            requireActivity().finish()
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