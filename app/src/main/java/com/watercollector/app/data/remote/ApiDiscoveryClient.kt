package com.watercollector.app.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class ApiDiscoveryClient {

    suspend fun discoverServer(timeoutMs: Int = 4000): String? {
        return withContext(Dispatchers.IO) {
            var socket: DatagramSocket? = null

            try {
                socket = DatagramSocket()
                socket.broadcast = true
                socket.soTimeout = timeoutMs

                val message = "WATER_COLLECTOR_DISCOVER".toByteArray(Charsets.UTF_8)

                val packet = DatagramPacket(
                    message,
                    message.size,
                    InetAddress.getByName("255.255.255.255"),
                    37020
                )

                socket.send(packet)

                val buffer = ByteArray(1024)
                val responsePacket = DatagramPacket(buffer, buffer.size)

                socket.receive(responsePacket)

                val response = String(
                    responsePacket.data,
                    0,
                    responsePacket.length,
                    Charsets.UTF_8
                )

                if (response.startsWith("WATER_COLLECTOR_API|")) {
                    response.removePrefix("WATER_COLLECTOR_API|").trim()
                } else {
                    null
                }
            } catch (e: Exception) {
                null
            } finally {
                socket?.close()
            }
        }
    }
}