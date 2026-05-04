package com.watercollector.app.ui.receipts

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.watercollector.app.CollectorApp
import com.watercollector.app.data.local.entities.LocalReceiptDraftEntity
import com.watercollector.app.data.repository.PrintRepository
import com.watercollector.app.databinding.FragmentLocalReceiptsBinding
import com.watercollector.app.printing.ReceiptPrintData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LocalReceiptsFragment : Fragment() {

    private var _binding: FragmentLocalReceiptsBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: LocalReceiptsAdapter

    private val bluetoothPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (!granted) {
                Toast.makeText(
                    requireContext(),
                    "صلاحية Bluetooth مطلوبة للطباعة",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

    private val viewModel: LocalReceiptsViewModel by viewModels {
        val container = (requireActivity().application as CollectorApp).appContainer

        LocalReceiptsViewModelFactory(
            repository = container.receiptRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocalReceiptsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)

        setupRecycler()
        setupSearch()
        observeData()
        requestBluetoothPermissionIfNeeded()
    }

    private fun setupRecycler() {
        adapter = LocalReceiptsAdapter(
            onPrintClick = { receipt ->
                askToPrint(receipt)
            }
        )

        binding.rvReceipts.layoutManager = LinearLayoutManager(requireContext())
        binding.rvReceipts.adapter = adapter
    }

    private fun setupSearch() {
        binding.etSearch.addTextChangedListener(
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
                    viewModel.setSearchQuery(s?.toString().orEmpty())
                }

                override fun afterTextChanged(s: Editable?) = Unit
            }
        )
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.items.collect { receipts ->
                adapter.submitList(receipts)

                binding.tvCount.text = "عدد الإيصالات: ${receipts.size}"

                binding.tvEmpty.visibility = if (receipts.isEmpty()) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            }
        }
    }

    private fun requestBluetoothPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bluetoothPermissionLauncher.launch(Manifest.permission.BLUETOOTH_CONNECT)
        }
    }

    private fun askToPrint(receipt: LocalReceiptDraftEntity) {
        AlertDialog.Builder(requireContext())
            .setTitle("طباعة الإيصال")
            .setMessage("هل تريد طباعة الإيصال رقم ${receipt.localReceiptNo}؟")
            .setPositiveButton("طباعة") { _, _ ->
                printReceipt(receipt)
            }
            .setNegativeButton("إلغاء", null)
            .show()
    }

    private fun printReceipt(receipt: LocalReceiptDraftEntity) {
        val container = (requireActivity().application as CollectorApp).appContainer
        val printRepository = container.printRepository
        val subscriberRepository = container.subscriberRepository

        viewLifecycleOwner.lifecycleScope.launch {
            try {
                val subscriberName = withContext(Dispatchers.IO) {
                    subscriberRepository
                        .getSubscriberById(receipt.subscriberId)
                        ?.subscriberName
                        ?: "العميل رقم ${receipt.subscriberId}"
                }

                val printData = ReceiptPrintData(
                    receiptNo = receipt.localReceiptNo,
                    dateTime = receipt.createdAt,
                    collectorName = container.sessionManager.collectorName
                        ?: container.sessionManager.fullName
                        ?: "المحصل",
                    subscriberId = receipt.subscriberId,
                    subscriberName = subscriberName,
                    amount = receipt.totalReceived,
                    paymentMethod = receipt.paymentMethod,
                    notes = receipt.notes
                )

                if (!printRepository.hasPrinter()) {
                    choosePrinterThenPrint(
                        printRepository = printRepository,
                        data = printData
                    )
                } else {
                    runPrint(
                        printRepository = printRepository,
                        data = printData
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