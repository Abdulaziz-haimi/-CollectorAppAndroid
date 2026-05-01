package com.watercollector.app.ui.subscriberdetails

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.watercollector.app.data.local.entities.LocalSubscriberMeterEntity
import com.watercollector.app.databinding.ItemSubscriberMeterBinding

class SubscriberMetersAdapter : ListAdapter<LocalSubscriberMeterEntity, SubscriberMetersAdapter.VH>(Diff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemSubscriberMeterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }
    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))
    class VH(private val binding: ItemSubscriberMeterBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: LocalSubscriberMeterEntity) {
            binding.tvMeterNo.text = item.meterNumber
            binding.tvMeterLocation.text = item.location ?: "بدون موقع"
            binding.tvPrimary.text = if (item.isPrimary) "أساسي" else "فرعي"
        }
    }
    class Diff : DiffUtil.ItemCallback<LocalSubscriberMeterEntity>() {
        override fun areItemsTheSame(oldItem: LocalSubscriberMeterEntity, newItem: LocalSubscriberMeterEntity) = oldItem.subscriberMeterId == newItem.subscriberMeterId
        override fun areContentsTheSame(oldItem: LocalSubscriberMeterEntity, newItem: LocalSubscriberMeterEntity) = oldItem == newItem
    }
}
