package com.watercollector.app.data.repository

import android.content.Context
import com.watercollector.app.printing.BluetoothPrinterDevice
import com.watercollector.app.printing.ReceiptPrintData
import com.watercollector.app.printing.ThermalPrinter

class PrintRepository(
    context: Context,
    private val sessionManager: SessionManager
) {
    private val printer = ThermalPrinter(context.applicationContext)

    fun getPairedPrinters(): List<BluetoothPrinterDevice> {
        return printer.getPairedPrinters()
    }

    fun savePrinter(address: String) {
        sessionManager.printerMacAddress = address
    }

    fun hasPrinter(): Boolean {
        return !sessionManager.printerMacAddress.isNullOrBlank()
    }

    fun print(data: ReceiptPrintData) {
        val mac = sessionManager.printerMacAddress
            ?: throw IllegalStateException("لم يتم اختيار طابعة")

        printer.printReceipt(mac, data)
    }
}