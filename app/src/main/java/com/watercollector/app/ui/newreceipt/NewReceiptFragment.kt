package com.watercollector.app.ui.newreceipt

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.watercollector.app.CollectorApp
import com.watercollector.app.data.repository.PrintRepository
import com.watercollector.app.databinding.FragmentNewReceiptBinding
import com.watercollector.app.printing.ReceiptPrintData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewReceiptFragment : Fragment() {

    private var _binding: FragmentNewReceiptBinding? = null
    private val binding get() = _binding!!

    private lateinit var allocationAdapter: InvoiceAllocationAdapter

    private data class PaymentMethodItem(
        val label: String,
        val code: String
    ) {
        override fun toString(): String = label
    }

    private val bluetoothPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                Toast.makeText(
                    requireContext(),
                    "صلاحية Bluetooth مطلوبة لطباعة الإيصال",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private val viewModel: NewReceiptViewModel by viewModels {
        val container = (requireActivity().application as CollectorApp).appContainer

        NewReceiptViewModelFactory(
            subscriberRepository = container.subscriberRepository,
            receiptRepository = container.receiptRepository,
            collectorId = container.sessionManager.collectorId,
            collectorName = container.sessionManager.collectorName
                ?: container.sessionManager.fullName
                ?: "المحصل رقم ${container.sessionManager.collectorId}"
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        val subscriberId = requireArguments().getInt("subscriberId")
        viewModel.load(subscriberId)

        binding.tvSubscriberId.text = "رقم المشترك: $subscriberId"

        setupSpinner()
        setupRecycler()
        setupListeners()
        observeData()
        requestBluetoothPermissionIfNeeded()
    }

    private fun setupSpinner() {
        val methods = listOf(
            PaymentMethodItem("نقد", "Cash"),
            PaymentMethodItem("حوالة", "Transfer"),
            PaymentMethodItem("محفظة", "Wallet"),
            PaymentMethodItem("شيك", "Cheque"),
            PaymentMethodItem("بطاقة", "Card"),
            PaymentMethodItem("أخرى", "Other")
        )

        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            methods
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spPaymentMethod.adapter = adapter
    }

    private fun setupRecycler() {
        allocationAdapter = InvoiceAllocationAdapter {
            updateSummary()
        }

        binding.rvAllocations.layoutManager = LinearLayoutManager(requireContext())
        binding.rvAllocations.adapter = allocationAdapter
    }

    private fun setupListeners() {
        binding.etAmount.addTextChangedListener(
            object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) = Unit

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                    updateSummary()
                }

                override fun afterTextChanged(s: Editable?) = Unit
            }
        )

        binding.btnAutoDistribute.setOnClickListener {
            val total = binding.etAmount.text
                ?.toString()
                ?.trim()
                ?.toDoubleOrNull()
                ?: 0.0

            allocationAdapter.setAutomaticAllocations(
                viewModel.autoDistribute(total)
            )

            updateSummary()
        }

        binding.btnSave.setOnClickListener {
            val total = binding.etAmount.text
                ?.toString()
                ?.trim()
                ?.toDoubleOrNull()
                ?: 0.0

            val selectedPaymentMethod =
                binding.spPaymentMethod.selectedItem as? PaymentMethodItem

            val paymentMethod = selectedPaymentMethod?.code ?: "Cash"

            val notes = binding.etNotes.text
                ?.toString()
                ?.trim()
                ?.takeIf { it.isNotBlank() }

            viewModel.saveDraft(
                totalReceived = total,
                paymentMethod = paymentMethod,
                notes = notes,
                allocations = allocationAdapter.getAllocations()
            )
        }
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
                Toast.makeText(
                    requireContext(),
                    message,
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.printRequest.collect { data ->
                askToPrint(data)
            }
        }
    }

    private fun updateSummary() {
        if (!::allocationAdapter.isInitialized) return

        val total = binding.etAmount.text
            ?.toString()
            ?.trim()
            ?.toDoubleOrNull()
            ?: 0.0

        val allocated = allocationAdapter.getAllocations()
            .values
            .sum()

        val remaining = maxOf(total - allocated, 0.0)

        binding.tvAllocationSummary.text =
            "الموزع: ${"%.2f".format(allocated)} | المتبقي كرصيد مقدم: ${"%.2f".format(remaining)}"
    }

    private fun requestBluetoothPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bluetoothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
        }
    }

    private fun askToPrint(data: ReceiptPrintData) {
        AlertDialog.Builder(requireContext())
            .setTitle("طباعة الإيصال")
            .setMessage("تم حفظ التحصيل محليًا. هل تريد طباعة الإيصال الآن؟")
            .setPositiveButton("طباعة") { _, _ ->
                printReceipt(data)
            }
            .setNegativeButton("لاحقًا", null)
            .show()
    }

    private fun printReceipt(data: ReceiptPrintData) {
        val container = (requireActivity().application as CollectorApp).appContainer
        val printRepository = container.printRepository

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                if (!printRepository.hasPrinter()) {
                    choosePrinterThenPrint(
                        printRepository = printRepository,
                        data = data
                    )
                } else {
                    runPrint(
                        printRepository = printRepository,
                        data = data
                    )
                }
            } catch (securityException: SecurityException) {
                requestBluetoothPermissionIfNeeded()

                Toast.makeText(
                    requireContext(),
                    securityException.message ?: "صلاحية Bluetooth مطلوبة للطباعة",
                    Toast.LENGTH_LONG
                ).show()
            } catch (exception: Exception) {
                Toast.makeText(
                    requireContext(),
                    exception.message ?: "فشل تجهيز الطباعة",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private suspend fun choosePrinterThenPrint(
        printRepository: PrintRepository,
        data: ReceiptPrintData
    ) {
        val printers = withContext(Dispatchers.IO) {
            printRepository.getPairedPrinters()
        }

        if (printers.isEmpty()) {
            Toast.makeText(
                requireContext(),
                "لا توجد طابعة Bluetooth مقترنة. اربط الطابعة من إعدادات الجوال أولًا.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        val printerNames = printers
            .map { printer ->
                "${printer.name}\n${printer.address}"
            }
            .toTypedArray()

        AlertDialog.Builder(requireContext())
            .setTitle("اختر الطابعة")
            .setItems(printerNames) { _, which ->
                printRepository.savePrinter(printers[which].address)

                viewLifecycleOwner.lifecycleScope.launch {
                    runPrint(
                        printRepository = printRepository,
                        data = data
                    )
                }
            }
            .show()
    }

    private suspend fun runPrint(
        printRepository: PrintRepository,
        data: ReceiptPrintData
    ) {
        try {
            withContext(Dispatchers.IO) {
                printRepository.print(data)
            }

            Toast.makeText(
                requireContext(),
                "تمت طباعة الإيصال",
                Toast.LENGTH_LONG
            ).show()
        } catch (exception: Exception) {
            Toast.makeText(
                requireContext(),
                exception.message ?: "فشلت طباعة الإيصال",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}