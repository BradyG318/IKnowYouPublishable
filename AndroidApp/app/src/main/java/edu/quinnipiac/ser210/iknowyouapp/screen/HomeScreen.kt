package com.example.navigationdrawer.screen

import android.Manifest
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import edu.quinnipiac.ser210.iknowyouapp.IKnowYouViewModel
import edu.quinnipiac.ser210.iknowyouapp.PersonCardData
import edu.quinnipiac.ser210.iknowyouapp.connection.BluetoothHelper
import kotlinx.coroutines.launch
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(iKnowYouViewModel: IKnowYouViewModel, snackbarHostState: SnackbarHostState, bluetoothHelper: BluetoothHelper) {

    val scope = rememberCoroutineScope()

    //var paired by remember { mutableStateOf(false) }
    var showDeviceDialog by remember { mutableStateOf(false) }
    var devices by remember { mutableStateOf(listOf<BluetoothDevice>()) }
    //var btSocket by remember { mutableStateOf<BluetoothSocket?>(null) }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        //containerColor = iKnowYouViewModel.backgroundColor
    ) { contentPadding ->
        Column(Modifier.fillMaxSize().padding(contentPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Welcome!", fontSize = 30.sp, color = MaterialTheme.colorScheme.outline)
            Spacer(modifier = Modifier.padding(10.dp))
            if(iKnowYouViewModel.paired){
                Text("Glasses paired! View gathered data in 'Data' and adjust settings in 'Glasses Settings'.", textAlign = TextAlign.Center)
                /*Button(
                    onClick = {
                        val testData = "hello world".toByteArray()
                        bluetoothHelper.sendData(
                            iKnowYouViewModel.btSocket,
                            testData,
                            onSuccess = {
                                scope.launch {
                                    snackbarHostState.showSnackbar("message sent")
                                }
                            },
                            onError = { error ->
                                scope.launch {
                                    snackbarHostState.showSnackbar("failed to send")
                                }
                            }
                        )
                        scope.launch {
                            snackbarHostState.showSnackbar("Sending message...")
                        }
                    }
                ) {
                    Text("Send test data", fontSize = 30.sp, color = MaterialTheme.colorScheme.secondary)
                }*/
            }else{
                Button(
                    onClick = {
                        // Create a new coroutine in the event handler to show a snackbar
                        bluetoothHelper.requestEnableBluetooth()
                        devices = bluetoothHelper.getPairedDevices()?.toList() ?: emptyList()
                        Log.d(TAG, "Bluetooth devices: " + bluetoothHelper.getPairedDevices().toString())
                        /*if(bluetoothAdapter?.isEnabled == false){
                            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                            startActivityForResult(enableBtIntent,REQUEST_ENABLE_BT)
                        }*/
                        scope.launch {
                            snackbarHostState.showSnackbar("Searching for devices...")
                        }
                        if(bluetoothHelper.getBluetoothAdapter()?.isEnabled == true){
                            showDeviceDialog = true
                        }
                    }
                ) {
                    Text("Pair Glasses", fontSize = 30.sp)
                }
            }

        }
    }

    if (showDeviceDialog) {
        AlertDialog(
            onDismissRequest = { showDeviceDialog = false },
            title = { Text("Select Bluetooth Device") },
            text = {
                Column {
                    devices.forEach { device ->
                        TextButton(
                            onClick = {
                                showDeviceDialog = false
                                val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // get Pi uuid
                                bluetoothHelper.connectToDevice(
                                    device,
                                    uuid,
                                    onConnected = { socket ->
                                        iKnowYouViewModel.changeSocket(socket)
                                        iKnowYouViewModel.changePaired(true)
                                        bluetoothHelper.startListening(
                                            socket = socket,
                                            onPersonReceived = { person ->
                                                iKnowYouViewModel.upsertPerson(
                                                    PersonCardData(
                                                        id = person.faceId,
                                                        name = person.fullName,
                                                        age = person.age,
                                                        imageBytes = person.imageBytes
                                                    )
                                                )
                                            },
                                            onError = { error ->
                                                scope.launch {
                                                    snackbarHostState.showSnackbar("Bluetooth receive failed: ${error.message}")
                                                }
                                            }
                                        )
                                        scope.launch {
                                            // checks if permissions granted (mostly because of annoying red squiggle, maybe check for simpler method)
//                                            if (ActivityCompat.checkSelfPermission(
//                                                    this as Context,
//                                                    Manifest.permission.BLUETOOTH_CONNECT
//                                                ) != PackageManager.PERMISSION_GRANTED
//                                            ) {
//                                                // TODO: Consider calling
//                                                //    ActivityCompat#requestPermissions
//                                                // here to request the missing permissions, and then overriding
//                                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                                                //                                          int[] grantResults)
//                                                // to handle the case where the user grants the permission. See the documentation
//                                                // for ActivityCompat#requestPermissions for more details.
//                                                return@launch
//                                            }
                                            snackbarHostState.showSnackbar(
                                                "Connected to ${device.name}"
                                            )
                                        }
                                        /*bluetoothHelper.startListening(
                                            socket,
                                            onDataReceived = {
                                                iKnowYouViewModel.personList
                                            },
                                            onError = {}
                                        )*/
                                    },
                                    onError = { error ->
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                "Connection failed: ${error.message}"
                                            )
                                        }
                                    }
                                )
                            }
                        ) {
                            Text(device.name ?: "Unknown Device")
                        }
                    }
                }
            },
            confirmButton = {}
        )
    }
}