package com.example.privacy_policy_lib

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.privacy_policy_lib.adapter.ContractsAdapter
import com.example.privacy_policy_lib.core.model.ContractItem
import com.example.privacy_policy_lib.core.utils.ContextUtils
import com.example.privacy_policy_lib.databinding.FragmentContractsBinding


class ContractsFragment: Fragment() {
    private var _binding: FragmentContractsBinding? = null
    private val binding get() = _binding!!
    private var mAdapter: ContractsAdapter? = null
    val contractItemList = arrayListOf(
        ContractItem("Gizlilik Politikası","file:///android_asset/privacypolicy/privacy_policy_tr.html"),
        ContractItem("Aydınlatma Metni", "file:///android_asset/privacypolicy/privacy_policy_en.html"),
        ContractItem("Ticari Elektronik İleti metni", "file:///android_asset/privacypolicy/privacy_policy_ar.html")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.let { ContextUtils.setmContext(it) }
        mAdapter = ContractsAdapter(requireActivity())
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }

}