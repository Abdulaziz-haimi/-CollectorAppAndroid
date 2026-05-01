package com.watercollector.app.ui.subscriberdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.watercollector.app.data.local.entities.LocalOpenInvoiceEntity
import com.watercollector.app.databinding.ItemSubscriberInvoiceBinding

class SubscriberInvoicesAdapter : ListAdapter<LocalOpenInvoiceEntity, SubscriberInvoicesAdapter.VH>(Diff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemSubscriberInvoiceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
    class VH(private val binding: ItemSubscriberInvoiceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LocalOpenInvoiceEntity) {
            binding.tvInvoiceNo.text = item.invoiceNumber ?: item.invoiceId.toString()
            binding.tvInvoiceDate.text = item.invoiceDate
            binding.tvRemaining.text = "المتبقي: ${"%.2f".format(item.remaining)}"
        }
    }
    class Diff : DiffUtil.ItemCallback<LocalOpenInvoiceEntity>() {
        override fun areItemsTheSame(oldItem: LocalOpenInvoiceEntity, newItem: LocalOpenInvoiceEntity) = oldItem.invoiceId == newItem.invoiceId
        override fun areContentsTheSame(oldItem: LocalOpenInvoiceEntity, newItem: LocalOpenInvoiceEntity) = oldItem == newItem
    }
}
