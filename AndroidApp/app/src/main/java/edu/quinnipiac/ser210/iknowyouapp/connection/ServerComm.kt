package edu.quinnipiac.ser210.iknowyouapp.connection

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.PrintWriter
import java.net.Socket

class ServerComm {
     fun sendMessage(host: String, port: Int, packet: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val socket = Socket(host,port)
                val writer = PrintWriter(socket.outputStream, true)
                val reader = BufferedReader(InputStreamReader(socket.inputStream))

                writer.println(packet)
                val response = reader.readLine()
                socket.close()

                response ?: "No response"

            }catch (e: Exception){
                e.printStackTrace()
                "Error: ${e.message}"
            }
        }
    }
}