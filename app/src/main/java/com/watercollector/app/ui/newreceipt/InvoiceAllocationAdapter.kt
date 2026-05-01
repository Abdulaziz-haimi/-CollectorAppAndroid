package com.watercollector.app.ui.newreceipt

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.watercollector.app.data.local.entities.LocalOpenInvoiceEntity
import com.watercollector.app.databinding.ItemInvoiceAllocationBinding

class InvoiceAllocationAdapter(
    private val onChanged: () -> Unit
) : ListAdapter<LocalOpenInvoiceEntity, InvoiceAllocationAdapter.VH>(Diff()) {

    private val allocations = mutableMapOf<Int, Double>()

    fun setAutomaticAllocations(newAllocations: Map<Int, Double>) {
        allocations.clear()
        allocations.putAll(newAllocations)
        notifyDataSetChanged()
        onChanged()
    }

    fun getAllocations(): Map<Int, Double> = allocations.toMap()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemInvoiceAllocationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    inner class VH(private val binding: ItemInvoiceAllocationBinding) : RecyclerView.ViewHolder(binding.root) {
        private var watcher: TextWatcher? = null

        fun bind(item: LocalOpenInvoiceEntity) {
            binding.tvInvoiceNo.text = item.invoiceNumber ?: item.invoiceId.toString()
            binding.tvInvoiceDate.text = item.invoiceDate
            binding.tvRemaining.text = "المتبقي: ${"%.2f".format(item.remaining)}"

            watcher?.let { binding.etAllocated.removeTextChangedListener(it) }
            binding.etAllocated.setText(allocations[item.invoiceId]?.takeIf { it > 0 }?.toString().orEmpty())
            watcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
                override fun afterTextChanged(s: Editable?) {
                    val value = s?.toString()?.trim()?.toDoubleOrNull() ?: 0.0
                    if (value <= 0.0) allocations.remove(item.invoiceId) else allocations[item.invoiceId] = value
                    onChanged()
                }
            }
            binding.etAllocated.addTextChangedListener(watcher)
        }
    }

    class Diff : DiffUtil.ItemCallback<LocalOpenInvoiceEntity>() {
        override fun areItemsTheSame(oldItem: LocalOpenInvoiceEntity, newItem: LocalOpenInvoiceEntity) = oldItem.invoiceId == newItem.invoiceId
        override fun areContentsTheSame(oldItem: LocalOpenInvoiceEntity, newItem: LocalOpenInvoiceEntity) = oldItem == newItem
    }
}
