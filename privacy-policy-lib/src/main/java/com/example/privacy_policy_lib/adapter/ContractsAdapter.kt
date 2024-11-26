package com.example.privacy_policy_lib.adapter


import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.privacy_policy_lib.PrivacyPolicyDialogFragment
import com.example.privacy_policy_lib.R
import com.example.privacy_policy_lib.core.model.ContractItem
import com.example.privacy_policy_lib.core.utils.ContextUtils
import com.example.privacy_policy_lib.databinding.ContractItemBinding

class ContractsAdapter(
    var mContext: FragmentActivity? = null
) : RecyclerView.Adapter<ContractsAdapter.ViewHolder>() {
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    private val contractsList: MutableList<ContractItem> = ArrayList()

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mContext = recyclerView.context as FragmentActivity?
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ContractItemBinding.inflate(mLayoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return contractsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contractItem = contractsList[position]
        holder.bind(contractItem)
        holder.txtContract.setOnClickListener {
            val fragment = PrivacyPolicyDialogFragment.newInstance(
                "https://www.logo.com.tr/logo-gizlilik-politikalari/mobilesales",
                contractItem.contractItemContent
            )
            (mContext as FragmentActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit()
        }
    }

    fun addItem(items: ArrayList<ContractItem>?) {
        if (items != null) {
            contractsList.clear()
            contractsList.addAll(items)
            notifyDataSetChanged()
        }
    }

    inner class ViewHolder(binding: ContractItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val txtContract : TextView = binding.txtContract
        fun bind(contractItem: ContractItem) {
            val firstString =  contractItem.contractItemText
            val secondString = ContextUtils.getStringResource(R.string.str_approve)
            val spannable = SpannableString("$firstString $secondString")

            spannable.setSpan(
                ForegroundColorSpan(Color.BLUE),
                0,
                firstString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable.setSpan(
                UnderlineSpan(),
                0,
                firstString.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            txtContract.text = spannable
        }
    }
}