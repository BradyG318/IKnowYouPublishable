package com.example.navigationdrawer.screen

import android.content.ContentValues.TAG
import android.os.Debug
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import edu.quinnipiac.ser210.iknowyouapp.IKnowYouViewModel
import edu.quinnipiac.ser210.iknowyouapp.connection.BluetoothHelper
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GlassesSettingScreen(iKnowYouViewModel: IKnowYouViewModel, bluetoothHelper: BluetoothHelper,snackbarHostState: SnackbarHostState) {
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    //var promptLength by remember { mutableStateOf(0) }
    Scaffold(
        //containerColor = iKnowYouViewModel.backgroundColor
    ) { contentPadding ->
        Column (
            Modifier.fillMaxWidth()
                .padding(top = 150.dp, start = 20.dp, end = 20.dp)
                .verticalScroll(scrollState)
        ) {
            Text("Display:")
            DisplaySwitch(iKnowYouViewModel)
            //Text("Show possible candidates:")
            //CandidateSwitch(iKnowYouViewModel)
            Text("Num people scanned:")
            NumSlider(iKnowYouViewModel)
            Text("UI Transparency:")
            TextSlider(iKnowYouViewModel)
            Text("Font scale")
            FontScaleSlider(iKnowYouViewModel)
            Text("Auto exposure:")
            AutoExposeSwitch(iKnowYouViewModel)
            Text("Manual exposure:")
            ExposureSlider(iKnowYouViewModel)
            Text("AI summary prompt:")
            TextField(
                value = iKnowYouViewModel.aiPrompt,
                onValueChange = {
                    if(it.length <= 100){
                        iKnowYouViewModel.aiPrompt = it
                        //promptLength = it.length
                    }
                },
                trailingIcon = {
                    Text("${iKnowYouViewModel.aiPrompt.length}/100")
                },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly){
                Button(
                    onClick = {
                        val data = SettingsPacket(
                            2,
                            iKnowYouViewModel.numScanned.toInt(),
                            //iKnowYouViewModel.possibleEnabled,
                            iKnowYouViewModel.displayEnabled,
                            iKnowYouViewModel.uiTransparency,
                            iKnowYouViewModel.fontScale,
                            iKnowYouViewModel.autoExpose,
                            iKnowYouViewModel.exposure,
                            iKnowYouViewModel.aiPrompt)
                        //Log.d(TAG, "Bluetooth devices: " + Json.encodeToString(data))
                        val packet = Json.encodeToString(data).toByteArray()
                        bluetoothHelper.sendData(
                            iKnowYouViewModel.btSocket,
                            packet,
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
                    }
                ) {
                    Text("Apply Settings")
                }
                Button(
                    onClick = {
                        iKnowYouViewModel.aiPrompt = ""
                        iKnowYouViewModel.numScanned = 2f
                        iKnowYouViewModel.fontScale = .55f
                        iKnowYouViewModel.displayEnabled = true
                        iKnowYouViewModel.uiTransparency = 1f
                        iKnowYouViewModel.autoExpose = true
                        iKnowYouViewModel.exposure = 7f
                        iKnowYouViewModel.possibleEnabled = true
                        //promptLength = 0
                        val data = SettingsPacket(
                            2,
                            iKnowYouViewModel.numScanned.toInt(),
                            //iKnowYouViewModel.possibleEnabled,
                            iKnowYouViewModel.displayEnabled,
                            iKnowYouViewModel.uiTransparency,
                            iKnowYouViewModel.fontScale,
                            iKnowYouViewModel.autoExpose,
                            iKnowYouViewModel.exposure,
                            iKnowYouViewModel.aiPrompt)
                        val packet = Json.encodeToString(data).toByteArray()
                        bluetoothHelper.sendData(
                            iKnowYouViewModel.btSocket,
                            packet,
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
                    }
                ) {
                    Text("Restore Default")
                }
            }

        }
    }
}

@Composable
fun NumSlider(viewModel: IKnowYouViewModel) {
    //var sliderPosition by remember { mutableFloatStateOf(0f) }
    Column {
        Slider(
            steps = 3,
            value = viewModel.numScanned,
            onValueChange = { viewModel.updateNumScanned(it) },
            valueRange = 1f..5f,
        )
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
            Text(text = viewModel.numScanned.roundToInt().toString())
            /*TextField(
                value = String.format("%.2f", viewModel.numScanned),
                onValueChange = {
                    viewModel.numScanned = it.toFloat()
                    if(viewModel.numScanned > 5){
                        viewModel.numScanned = 5f
                    }
                    if(viewModel.numScanned < 1){
                        viewModel.numScanned = 1f
                    }
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(100.dp)
            )*/
        }

    }
}

@Composable
fun CandidateSwitch(iKnowYouViewModel: IKnowYouViewModel) {
    //var checked by remember { mutableStateOf(true) }

    Switch(
        checked = iKnowYouViewModel.possibleEnabled,
        onCheckedChange = {
            iKnowYouViewModel.updatePossibleEnabled(it)
        }
    )
}

@Composable
fun DisplaySwitch(iKnowYouViewModel: IKnowYouViewModel) {
    //var checked by remember { mutableStateOf(true) }
    Switch(
        checked = iKnowYouViewModel.displayEnabled,
        onCheckedChange = {
            iKnowYouViewModel.updateDisplayEnabled(it)
        }
    )
}

@Composable
fun TextSlider(viewModel: IKnowYouViewModel) {
    //var sliderPosition by remember { mutableFloatStateOf(0f) }
    Column {
        Slider(
            //steps = 3,
            value = viewModel.uiTransparency,
            onValueChange = { viewModel.updateTextAlpha(it) },
            valueRange = 0f..1f,
        )
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextField(
                value = String.format("%.2f", viewModel.uiTransparency),
                onValueChange = {
                    viewModel.uiTransparency = it.toFloat()
                    if(viewModel.uiTransparency > 1){
                        viewModel.uiTransparency = 1f
                    }
                    if(viewModel.uiTransparency < 0){
                        viewModel.uiTransparency = 0f
                    }
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(100.dp)
            )
        }
    }
}

@Composable
fun AutoExposeSwitch(iKnowYouViewModel: IKnowYouViewModel) {
    //var checked by remember { mutableStateOf(true) }
    Switch(
        checked = iKnowYouViewModel.autoExpose,
        onCheckedChange = {
            iKnowYouViewModel.autoExpose = it
        }
    )
}

@Composable
fun ExposureSlider(viewModel: IKnowYouViewModel) {
    Column {
        Slider(
            //steps = 3,
            value = viewModel.exposure,
            onValueChange = { viewModel.exposure = it },
            valueRange = 0f..13f,
        )
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextField(
                value = String.format("%.2f", viewModel.exposure),
                onValueChange = {
                    viewModel.exposure = it.toFloat()
                    if(viewModel.exposure > 13){
                        viewModel.exposure = 13f
                    }
                    if(viewModel.exposure < 0){
                        viewModel.exposure = 0f
                    }
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(100.dp)
            )
        }
    }
}

@Composable
fun FontScaleSlider(viewModel: IKnowYouViewModel) {
    Column {
        Slider(
            value = viewModel.fontScale,
            onValueChange = { viewModel.fontScale = it.toFloat() },
            valueRange = .3f..2f,
        )
        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            TextField(
                value = String.format("%.2f", viewModel.fontScale),
                onValueChange = {
                    viewModel.fontScale = it.toFloat()
                    if(viewModel.fontScale > 2){
                        viewModel.fontScale = 2f
                    }
                    if(viewModel.uiTransparency < .3){
                        viewModel.uiTransparency = .3f
                    }
                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(100.dp)
            )
        }
    }
}

@Serializable
data class SettingsPacket(
    val id: Int,
    val numPeople: Int,
    //val showPotential: Boolean,
    val showDisplay: Boolean,
    val uiTransparency: Float,
    val fontScale: Float,
    val autoExposeOn: Boolean,
    val manualExposure: Float,
    val aiPrompt: String
)