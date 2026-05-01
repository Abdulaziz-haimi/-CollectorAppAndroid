package com.watercollector.app.ui.receipts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.watercollector.app.data.local.entities.LocalReceiptDraftEntity
import com.watercollector.app.databinding.ItemLocalReceiptBinding

class LocalReceiptsAdapter : ListAdapter<LocalReceiptDraftEntity, LocalReceiptsAdapter.VH>(Diff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemLocalReceiptBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
    class VH(private val binding: ItemLocalReceiptBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LocalReceiptDraftEntity) {
            binding.tvReceiptNo.text = item.localReceiptNo
            binding.tvAmount.text = "${"%.2f".format(item.totalReceived)}"
            binding.tvDate.text = item.paymentDate
            binding.tvStatus.text = item.syncStatus
            binding.tvReason.text = item.rejectedReason ?: ""
        }
    }
    class Diff : DiffUtil.ItemCallback<LocalReceiptDraftEntity>() {
        override fun areItemsTheSame(oldItem: LocalReceiptDraftEntity, newItem: LocalReceiptDraftEntity) = oldItem.localReceiptId == newItem.localReceiptId
        override fun areContentsTheSame(oldItem: LocalReceiptDraftEntity, newItem: LocalReceiptDraftEntity) = oldItem == newItem
    }
}
