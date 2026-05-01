package com.watercollector.app.ui.newreceipt

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.watercollector.app.CollectorApp
import com.watercollector.app.databinding.FragmentNewReceiptBinding
import kotlinx.coroutines.launch

class NewReceiptFragment : Fragment() {
    private var _binding: FragmentNewReceiptBinding? = null
    private val binding get() = _binding!!
    private lateinit var allocationAdapter: InvoiceAllocationAdapter

    private val viewModel: NewReceiptViewModel by viewModels {
        val container = (requireActivity().application as CollectorApp).appContainer
        NewReceiptViewModelFactory(
            container.subscriberRepository,
            container.receiptRepository,
            container.sessionManager.collectorId
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNewReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val subscriberId = requireArguments().getInt("subscriberId")
        viewModel.load(subscriberId)

        binding.tvSubscriberId.text = "SubscriberID: $subscriberId"
        setupSpinner()
        setupRecycler()
        observeData()

        binding.btnAutoDistribute.setOnClickListener {
            val total = binding.etAmount.text?.toString()?.trim()?.toDoubleOrNull() ?: 0.0
            allocationAdapter.setAutomaticAllocations(viewModel.autoDistribute(total))
            updateSummary()
        }

        binding.btnSave.setOnClickListener {
            val total = binding.etAmount.text?.toString()?.trim()?.toDoubleOrNull() ?: 0.0
            val paymentMethod = binding.spPaymentMethod.selectedItem?.toString().orEmpty().ifBlank { "Cash" }
            val notes = binding.etNotes.text?.toString()?.trim()
            viewModel.saveDraft(total, paymentMethod, notes, allocationAdapter.getAllocations())
        }
    }

    private fun setupSpinner() {
        val methods = listOf("Cash", "Transfer", "Cheque", "Other")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, methods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spPaymentMethod.adapter = adapter
    }

    private fun setupRecycler() {
        allocationAdapter = InvoiceAllocationAdapter { updateSummary() }
        binding.rvAllocations.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAllocations.adapter = allocationAdapter
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.invoices.collect { invoices ->
                allocationAdapter.submitList(invoices)
                binding.tvInvoicesHint.text = if (invoices.isEmpty()) {
                    "لا توجد فواتير مفتوحة. أي مبلغ سيُحفظ كرصيد مقدم."
                } else {
                    "يمكنك توزيع المبلغ يدويًا على عدة فواتير أو استخدام التوزيع التلقائي."
                }
                updateSummary()
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.saveMessage.collect { message ->
                if (!message.isNullOrBlank()) Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun updateSummary() {
        val total = binding.etAmount.text?.toString()?.trim()?.toDoubleOrNull() ?: 0.0
        val allocated = allocationAdapter.getAllocations().values.sum()
        val remaining = total - allocated
        binding.tvAllocationSummary.text = "الموزع: ${"%.2f".format(allocated)} | المتبقي كرصد مقدم: ${"%.2f".format(maxOf(remaining, 0.0))}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
