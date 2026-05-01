package com.watercollector.app.ui.subscriberdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.watercollector.app.data.local.entities.LocalSubscriberCreditEntity
import com.watercollector.app.databinding.ItemSubscriberCreditBinding

class SubscriberCreditsAdapter : ListAdapter<LocalSubscriberCreditEntity, SubscriberCreditsAdapter.VH>(Diff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemSubscriberCreditBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
    class VH(private val binding: ItemSubscriberCreditBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LocalSubscriberCreditEntity) {
            binding.tvCreditDate.text = item.creditDate
            binding.tvCreditRemaining.text = "المتبقي: ${"%.2f".format(item.amountRemaining)}"
        }
    }
    class Diff : DiffUtil.ItemCallback<LocalSubscriberCreditEntity>() {
        override fun areItemsTheSame(oldItem: LocalSubscriberCreditEntity, newItem: LocalSubscriberCreditEntity) = oldItem.creditId == newItem.creditId
        override fun areContentsTheSame(oldItem: LocalSubscriberCreditEntity, newItem: LocalSubscriberCreditEntity) = oldItem == newItem
    }
}
