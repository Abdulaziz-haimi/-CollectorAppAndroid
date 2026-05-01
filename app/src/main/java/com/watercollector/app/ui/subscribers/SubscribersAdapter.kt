package com.watercollector.app.ui.subscribers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.watercollector.app.data.local.entities.LocalSubscriberEntity
import com.watercollector.app.databinding.ItemSubscriberBinding
import kotlin.math.abs

class SubscribersAdapter(
    private val onClick: (LocalSubscriberEntity) -> Unit
) : ListAdapter<LocalSubscriberEntity, SubscribersAdapter.VH>(Diff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemSubscriberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    inner class VH(private val binding: ItemSubscriberBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LocalSubscriberEntity) {
            binding.tvName.text = item.subscriberName
            binding.tvPhone.text = item.phoneNumber ?: "بدون رقم"
            binding.tvMeter.text = item.primaryMeterNumber ?: "بدون عداد أساسي"
            binding.tvBalance.text = when {
                item.currentBalance > 0 -> "عليه ${"%.2f".format(item.currentBalance)}"
                item.currentBalance < 0 -> "له ${"%.2f".format(abs(item.currentBalance))}"
                else -> "متوازن"
            }
            binding.root.setOnClickListener { onClick(item) }
        }
    }

    class Diff : DiffUtil.ItemCallback<LocalSubscriberEntity>() {
        override fun areItemsTheSame(oldItem: LocalSubscriberEntity, newItem: LocalSubscriberEntity) = oldItem.subscriberId == newItem.subscriberId
        override fun areContentsTheSame(oldItem: LocalSubscriberEntity, newItem: LocalSubscriberEntity) = oldItem == newItem
    }
}
