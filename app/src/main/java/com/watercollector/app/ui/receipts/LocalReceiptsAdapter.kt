package com.watercollector.app.ui.receipts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.watercollector.app.data.local.entities.LocalReceiptDraftEntity
import com.watercollector.app.databinding.ItemLocalReceiptBinding

class LocalReceiptsAdapter(
    private val onPrintClick: (LocalReceiptDraftEntity) -> Unit
) : ListAdapter<LocalReceiptDraftEntity, LocalReceiptsAdapter.VH>(Diff()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VH {
        val binding = ItemLocalReceiptBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        return VH(
            binding = binding,
            onPrintClick = onPrintClick
        )
    }

    override fun onBindViewHolder(
        holder: VH,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    class VH(
        private val binding: ItemLocalReceiptBinding,
        private val onPrintClick: (LocalReceiptDraftEntity) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: LocalReceiptDraftEntity) {
            binding.tvReceiptNo.text = "رقم الإيصال: ${item.localReceiptNo}"
            binding.tvSubscriberId.text = "رقم العميل: ${item.subscriberId}"
            binding.tvAmount.text = "المبلغ: ${"%,.0f".format(item.totalReceived)} ريال"
            binding.tvDate.text = "التاريخ: ${formatDate(item.createdAt)}"
            binding.tvPaymentMethod.text = "نوع السداد: ${arabicPaymentMethod(item.paymentMethod)}"
            binding.tvStatus.text = "الحالة: ${arabicSyncStatus(item.syncStatus)}"
            binding.tvReason.text = item.rejectedReason.orEmpty()

            binding.btnPrint.setOnClickListener {
                onPrintClick(item)
            }
        }

        private fun formatDate(value: String): String {
            return value.replace("T", " ")
        }

        private fun arabicPaymentMethod(method: String): String {
            return when (method.trim().lowercase()) {
                "cash", "نقد", "نقدًا" -> "نقد"
                "transfer", "حوالة" -> "حوالة"
                "wallet", "محفظة" -> "محفظة"
                "cheque", "check", "شيك" -> "شيك"
                "card", "بطاقة" -> "بطاقة"
                "bank", "تحويل بنكي" -> "تحويل بنكي"
                "other", "أخرى" -> "أخرى"
                else -> method
            }
        }

        private fun arabicSyncStatus(status: String): String {
            return when (status.trim().lowercase()) {
                "pending" -> "غير مزامن"
                "sent" -> "مرسل"
                "accepted" -> "مقبول"
                "rejected" -> "مرفوض"
                "synced" -> "تمت المزامنة"
                else -> status
            }
        }
    }

    class Diff : DiffUtil.ItemCallback<LocalReceiptDraftEntity>() {
        override fun areItemsTheSame(
            oldItem: LocalReceiptDraftEntity,
            newItem: LocalReceiptDraftEntity
        ): Boolean {
            return oldItem.localReceiptId == newItem.localReceiptId
        }

        override fun areContentsTheSame(
            oldItem: LocalReceiptDraftEntity,
            newItem: LocalReceiptDraftEntity
        ): Boolean {
            return oldItem == newItem
        }
    }
}