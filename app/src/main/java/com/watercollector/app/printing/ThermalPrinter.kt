package com.watercollector.app.printing

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextDirectionHeuristics
import android.text.TextPaint
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.util.Locale
import java.util.UUID
import kotlin.math.ceil

class ThermalPrinter(
    private val context: Context
) {
    private val sppUuid: UUID =
        UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    @SuppressLint("MissingPermission")
    fun getPairedPrinters(): List<BluetoothPrinterDevice> {
        ensureBluetoothPermission()

        val adapter = BluetoothAdapter.getDefaultAdapter()
            ?: throw IllegalStateException("الجهاز لا يدعم Bluetooth")

        if (!adapter.isEnabled) {
            throw IllegalStateException("Bluetooth غير مفعل")
        }

        return adapter.bondedDevices
            .map { device ->
                BluetoothPrinterDevice(
                    name = device.name ?: "Unknown Printer",
                    address = device.address
                )
            }
            .sortedBy { it.name }
    }

    @SuppressLint("MissingPermission")
    fun printReceipt(
        macAddress: String,
        data: ReceiptPrintData
    ) {
        ensureBluetoothPermission()

        if (macAddress.isBlank()) {
            throw IllegalArgumentException("عنوان الطابعة غير صحيح")
        }

        val adapter = BluetoothAdapter.getDefaultAdapter()
            ?: throw IllegalStateException("الجهاز لا يدعم Bluetooth")

        if (!adapter.isEnabled) {
            throw IllegalStateException("Bluetooth غير مفعل")
        }

        val device: BluetoothDevice = adapter.getRemoteDevice(macAddress)
        val socket = device.createRfcommSocketToServiceRecord(sppUuid)

        adapter.cancelDiscovery()

        socket.use { bluetoothSocket ->
            bluetoothSocket.connect()

            val output: OutputStream = bluetoothSocket.outputStream

            // Initialize printer
            output.write(byteArrayOf(0x1B, 0x40))

            val bitmap = ArabicReceiptBitmapBuilder.createReceiptBitmap(
                data = data,
                paperWidthPx = 384 // 58mm. إذا الطابعة 80mm غيّرها إلى 576
            )

            output.write(bitmapToEscPos(bitmap))

            // Feed paper
            output.write(byteArrayOf(0x0A, 0x0A, 0x0A))

            // Cut paper if supported
            output.write(byteArrayOf(0x1D, 0x56, 0x42, 0x00))

            output.flush()
        }
    }

    private fun bitmapToEscPos(bitmap: Bitmap): ByteArray {
        val width = bitmap.width
        val height = bitmap.height

        val bytesPerRow = ceil(width / 8.0).toInt()
        val imageBytes = ByteArray(bytesPerRow * height)

        var index = 0

        for (y in 0 until height) {
            for (xByte in 0 until bytesPerRow) {
                var byteValue = 0

                for (bit in 0..7) {
                    val x = xByte * 8 + bit

                    if (x < width) {
                        val pixel = bitmap.getPixel(x, y)

                        val red = Color.red(pixel)
                        val green = Color.green(pixel)
                        val blue = Color.blue(pixel)

                        val gray = (red + green + blue) / 3

                        if (gray < 160) {
                            byteValue = byteValue or (1 shl (7 - bit))
                        }
                    }
                }

                imageBytes[index++] = byteValue.toByte()
            }
        }

        val command = ByteArrayOutputStream()

        // GS v 0 raster bitmap command
        command.write(0x1D)
        command.write(0x76)
        command.write(0x30)
        command.write(0x00)

        command.write(bytesPerRow and 0xFF)
        command.write((bytesPerRow shr 8) and 0xFF)

        command.write(height and 0xFF)
        command.write((height shr 8) and 0xFF)

        command.write(imageBytes)

        return command.toByteArray()
    }

    private fun ensureBluetoothPermission() {
        if (!hasBluetoothPermission()) {
            throw SecurityException("يجب منح صلاحية Bluetooth للطباعة")
        }
    }

    private fun hasBluetoothPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }
}

data class BluetoothPrinterDevice(
    val name: String,
    val address: String
)

data class ReceiptPrintData(
    val receiptNo: String,
    val dateTime: String,
    val collectorName: String,
    val subscriberId: Int,
    val subscriberName: String,
    val amount: Double,
    val paymentMethod: String,
    val notes: String?
)

object ArabicReceiptBitmapBuilder {

    fun createReceiptBitmap(
        data: ReceiptPrintData,
        paperWidthPx: Int = 384
    ): Bitmap {
        val padding = 18
        val contentWidth = paperWidthPx - padding * 2

        val titlePaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 34f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val bodyPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 26f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        }

        val amountPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.BLACK
            textSize = 30f
            typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }

        val rows = listOf(
            ReceiptRow.Title("شعار الشركة"),
            ReceiptRow.Line,
            ReceiptRow.Text("التاريخ: ${formatDate(data.dateTime)}"),
            ReceiptRow.Text("رقم الإيصال: ${data.receiptNo}"),
            ReceiptRow.Text("اسم العميل: ${data.subscriberName}"),
            ReceiptRow.Amount("المبلغ: ${formatAmount(data.amount)} ريال"),
            ReceiptRow.Text("نوع السداد: ${arabicPaymentMethod(data.paymentMethod)}"),
            ReceiptRow.Line
        )

        var totalHeight = padding

        rows.forEach { row ->
            totalHeight += when (row) {
                is ReceiptRow.Title ->
                    measureTextHeight(row.value, titlePaint, contentWidth) + 18

                is ReceiptRow.Text ->
                    measureTextHeight(row.value, bodyPaint, contentWidth) + 12

                is ReceiptRow.Amount ->
                    measureTextHeight(row.value, amountPaint, contentWidth) + 16

                ReceiptRow.Line -> 24
            }
        }

        totalHeight += padding

        val bitmap = Bitmap.createBitmap(
            paperWidthPx,
            totalHeight,
            Bitmap.Config.ARGB_8888
        )

        val canvas = Canvas(bitmap)
        canvas.drawColor(Color.WHITE)

        var y = padding

        rows.forEach { row ->
            when (row) {
                is ReceiptRow.Title -> {
                    y += drawArabicText(
                        canvas = canvas,
                        text = row.value,
                        paint = titlePaint,
                        x = padding,
                        y = y,
                        width = contentWidth,
                        align = Layout.Alignment.ALIGN_CENTER
                    ) + 18
                }

                is ReceiptRow.Text -> {
                    y += drawArabicText(
                        canvas = canvas,
                        text = row.value,
                        paint = bodyPaint,
                        x = padding,
                        y = y,
                        width = contentWidth,
                        align = Layout.Alignment.ALIGN_OPPOSITE
                    ) + 12
                }

                is ReceiptRow.Amount -> {
                    y += drawArabicText(
                        canvas = canvas,
                        text = row.value,
                        paint = amountPaint,
                        x = padding,
                        y = y,
                        width = contentWidth,
                        align = Layout.Alignment.ALIGN_OPPOSITE
                    ) + 16
                }

                ReceiptRow.Line -> {
                    val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = Color.BLACK
                        strokeWidth = 2f
                    }

                    canvas.drawLine(
                        padding.toFloat(),
                        (y + 10).toFloat(),
                        (paperWidthPx - padding).toFloat(),
                        (y + 10).toFloat(),
                        linePaint
                    )

                    y += 24
                }
            }
        }

        return bitmap
    }

    private fun drawArabicText(
        canvas: Canvas,
        text: String,
        paint: TextPaint,
        x: Int,
        y: Int,
        width: Int,
        align: Layout.Alignment
    ): Int {
        val layout = StaticLayout.Builder
            .obtain(text, 0, text.length, paint, width)
            .setAlignment(align)
            .setTextDirection(TextDirectionHeuristics.RTL)
            .setLineSpacing(0f, 1.0f)
            .setIncludePad(false)
            .build()

        canvas.save()
        canvas.translate(x.toFloat(), y.toFloat())
        layout.draw(canvas)
        canvas.restore()

        return layout.height
    }

    private fun measureTextHeight(
        text: String,
        paint: TextPaint,
        width: Int
    ): Int {
        return StaticLayout.Builder
            .obtain(text, 0, text.length, paint, width)
            .setAlignment(Layout.Alignment.ALIGN_OPPOSITE)
            .setTextDirection(TextDirectionHeuristics.RTL)
            .setLineSpacing(0f, 1.0f)
            .setIncludePad(false)
            .build()
            .height
    }

    private fun formatAmount(amount: Double): String {
        return "%,.0f".format(amount)
    }

    private fun formatDate(value: String): String {
        return value.replace("T", " ")
    }

    private fun arabicPaymentMethod(method: String): String {
        return when (method.trim().lowercase(Locale.US)) {
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
}

sealed class ReceiptRow {
    data class Title(val value: String) : ReceiptRow()
    data class Text(val value: String) : ReceiptRow()
    data class Amount(val value: String) : ReceiptRow()
    object Line : ReceiptRow()
}