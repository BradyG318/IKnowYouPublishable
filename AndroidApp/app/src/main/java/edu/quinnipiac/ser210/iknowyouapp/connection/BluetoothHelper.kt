package edu.quinnipiac.ser210.iknowyouapp.connection

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.bluetooth.BluetoothSocket
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.pm.PackageManager
import android.health.connect.datatypes.Device
import androidx.core.app.ActivityCompat
import java.io.InputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder

import java.util.UUID
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.SecretKeySpec

class BluetoothHelper (private val context: Context, private val activity: Activity){
    private val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private val bluetoothAdapter: BluetoothAdapter? = bluetoothManager.adapter
    private var bluetoothGatt: BluetoothGatt? = null
    //@RequiresApi(Build.VERSION_CODES.S)
    fun requestEnableBluetooth() {
        if (bluetoothAdapter?.isEnabled == false) {
            val permissions = arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )

            ActivityCompat.requestPermissions(activity, permissions, 1001)
        }
    }
    private fun readExactly(input: InputStream, size: Int): ByteArray {
        val buffer = ByteArray(size)
        var offset = 0

        while (offset < size) {
            val count = input.read(buffer, offset, size - offset)
            if (count == -1) {
                throw Exception("Bluetooth stream closed")
            }
            offset += count
        }

        return buffer
    }

    fun startListening(
        socket: BluetoothSocket?,
        onPersonReceived: (RecognizedPerson) -> Unit,
        onError: (Exception) -> Unit = {}
    ) {
        Thread {
            try {
                val input = socket?.inputStream ?: throw Exception("Socket input stream is null")

                while (true) {
                    val lengthBytes = readExactly(input, 4)
                    val payloadLength = ByteBuffer.wrap(lengthBytes)
                        .order(ByteOrder.BIG_ENDIAN)
                        .int

                    val payload = readExactly(input, payloadLength)
                    val person = parseRecognizedPersonPacket(payload)

                    activity.runOnUiThread {
                        onPersonReceived(person)
                    }
                }
            } catch (e: Exception) {
                activity.runOnUiThread {
                    onError(e)
                }
            }
        }.start()
    }

    private fun parseRecognizedPersonPacket(payload: ByteArray): RecognizedPerson {
        val buffer = ByteBuffer.wrap(payload).order(ByteOrder.BIG_ENDIAN)

        val packetType = buffer.get().toInt() and 0xFF
        require(packetType == 1) { "Unknown packet type: $packetType" }

        val trackId = buffer.int
        val faceId = buffer.int
        val age = buffer.int
        val nameLen = buffer.int
        val imageLen = buffer.int

        val nameBytes = ByteArray(nameLen)
        buffer.get(nameBytes)

        val imageBytes = ByteArray(imageLen)
        buffer.get(imageBytes)

        return RecognizedPerson(
            trackId = trackId,
            faceId = faceId,
            age = age,
            fullName = String(nameBytes, Charsets.UTF_8),
            imageBytes = imageBytes
        )
    }

    fun getPairedDevices(): Set<BluetoothDevice>? {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
        ) != PackageManager.PERMISSION_GRANTED) {
            return null
        }

        return bluetoothAdapter?.bondedDevices
    }

    fun connectToDevice(
        device: BluetoothDevice,
        uuid: UUID,
        onConnected: (BluetoothSocket) -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onError(SecurityException("BLUETOOTH_CONNECT permission not granted"))
            return
        }

        Thread {
            try {
                val socket = device.createRfcommSocketToServiceRecord(uuid)

                bluetoothAdapter?.cancelDiscovery()

                socket.connect()

                activity.runOnUiThread {
                    onConnected(socket)
                }

            } catch (e: Exception) {
                activity.runOnUiThread {
                    onError(e)
                }
            }
        }.start()
    }

    fun sendData(
        socket: BluetoothSocket?,
        data: ByteArray,
        onSuccess: () -> Unit = {},
        onError: (Exception) -> Unit = {}
    ) {
        Thread {
            try {
                val outputStream = socket?.outputStream
                outputStream?.write(data)
                outputStream?.flush()

                activity.runOnUiThread {
                    onSuccess()
                }
            } catch (e: Exception) {
                activity.runOnUiThread {
                    onError(e)
                }
            }
        }.start()
    }

    fun decrypt(
        key: ByteArray,
        nonce: ByteArray,
        ciphertext: ByteArray
    ): ByteArray {
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        val spec = GCMParameterSpec(128, nonce)
        val keySpec = SecretKeySpec(key, "AES")

        cipher.init(Cipher.DECRYPT_MODE, keySpec, spec)
        return cipher.doFinal(ciphertext)
    }

    /*fun startListening(
        socket: BluetoothSocket?,
        onDataReceived: (ByteArray) -> Unit,
        onError: (Exception) -> Unit
    ) {
        Thread {
            try {
                val inputStream = socket?.inputStream ?: return@Thread
                val buffer = ByteArray(1024)

                while (true) {
                    val bytes = inputStream.read(buffer)

                    if (bytes > 0) {
                        val data = buffer.copyOf(bytes)

                        activity.runOnUiThread {
                            onDataReceived(data)
                        }
                    }
                }
            } catch (e: Exception) {
                activity.runOnUiThread {
                    onError(e)
                }
            }
        }.start()
    }*/

    /*fun BleScan(onDeviceFound: (BluetoothDevice) -> Unit){
        val scanner = bluetoothAdapter?.bluetoothLeScanner ?: return
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED){
            return
        }

        val scanCallback = object : ScanCallback(){
            override fun onScanResult(callbackType: Int, result: ScanResult) {
                result.device?.let { onDeviceFound(it) }
            }
        }

        scanner.startScan(scanCallback)
    }

    fun connectToBleDevice(
        device: BluetoothDevice,
        onConnected: () -> Unit,
        onDisconnected: () -> Unit,
        onServicesReady: (BluetoothGatt) -> Unit,
        onError: (Exception) -> Unit
    ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            onError(SecurityException("Missing BLUETOOTH_CONNECT permission"))
            return
        }

        bluetoothGatt = device.connectGatt(context, false, object : BluetoothGattCallback() {

            override fun onConnectionStateChange(
                gatt: BluetoothGatt,
                status: Int,
                newState: Int
            ) {
                if (newState == BluetoothProfile.STATE_CONNECTED) {
                    gatt.discoverServices()
                    activity.runOnUiThread { onConnected() }

                } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                    activity.runOnUiThread { onDisconnected() }
                }
            }

            override fun onServicesDiscovered(gatt: BluetoothGatt, status: Int) {
                if (status == BluetoothGatt.GATT_SUCCESS) {
                    activity.runOnUiThread {
                        onServicesReady(gatt)
                    }
                }
            }
        })
    }*/

    fun getBluetoothManager(): BluetoothManager{
        return bluetoothManager
    }

    fun getBluetoothAdapter(): BluetoothAdapter?{
        return bluetoothAdapter
    }

    /*fun sendMessage(){
        try {
            mmOutStream.write(bytes)
        } catch (e: IOException) {
            Log.e(TAG, "Error occurred when sending data", e)

            // Send a failure message back to the activity.
            val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
            val bundle = Bundle().apply {
                putString("toast", "Couldn't send data to the other device")
            }
            writeErrorMsg.data = bundle
            handler.sendMessage(writeErrorMsg)
            return
        }

        // Share the sent message with the UI activity.
        val writtenMsg = handler.obtainMessage(
            MESSAGE_WRITE, -1, -1, mmBuffer)
        writtenMsg.sendToTarget()
    }*/

    /*private inner class ConnectThread{
        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.cre
        }
    }*/
}