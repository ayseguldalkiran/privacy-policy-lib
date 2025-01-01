package com.example.privacy_policy_lib.adapter


import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.privacy_policy_lib.R
import com.example.privacy_policy_lib.core.model.ContractItem
import com.example.privacy_policy_lib.core.utils.ContextUtils
import com.example.privacy_policy_lib.databinding.ContractItemBinding

class ContractsAdapter(
    var mContext: FragmentActivity? = null,
    private val listener: OnAllCheckboxCheckedListener
) : RecyclerView.Adapter<ContractsAdapter.ViewHolder>() {
    private val mLayoutInflater: LayoutInflater = LayoutInflater.from(mContext)
    private val contractsList: MutableList<ContractItem> = ArrayList()
    private var checkboxStates = BooleanArray(0)
    internal var onContractClicked: (position: Int) -> Unit = { _-> }

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
        holder.bind(contractItem) {  ->
            onContractClicked(position)
        }
        holder.chkBoxContract.isChecked = checkboxStates[position]
        holder.chkBoxContract.setOnCheckedChangeListener { _, isChecked ->
            checkboxStates[position] = isChecked
            listener.onAllCheckboxChecked(checkboxStates.all { it })
        }
    }

    fun addItem(items: ArrayList<ContractItem>?) {
        if (items != null) {
            contractsList.clear()
            contractsList.addAll(items)
            checkboxStates = BooleanArray(items.size) { false }
            notifyDataSetChanged()
        }
    }

    fun updateCheckboxStates(states: BooleanArray) {
        checkboxStates = states.copyOf()
        notifyDataSetChanged()
    }

    inner class ViewHolder(binding: ContractItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val txtContract : TextView = binding.txtContract
        val chkBoxContract : CheckBox = binding.chkContract
        fun bind(
            contractItem: ContractItem,
            onContractClicked: () -> Unit
        ) {
            val firstString = contractItem.contractItemText
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
            txtContract.setOnClickListener {
                onContractClicked()
            }
        }
    }

    interface OnAllCheckboxCheckedListener {
        fun onAllCheckboxChecked(isChecked: Boolean)
    }
}