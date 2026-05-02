package com.example.navigationdrawer.screen

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.quinnipiac.ser210.iknowyouapp.IKnowYouViewModel
import edu.quinnipiac.ser210.iknowyouapp.ui.theme.AppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun SettingScreen(iKnowYouViewModel: IKnowYouViewModel) {
    val isExpanded = remember { mutableStateOf(false) }
    //val colors = listOf("Light","Dark", "Default", "Yellow", "Purple", "Green") // pre-defined list of Colors
    val defaultColor = MaterialTheme.colorScheme.secondary
    @Composable
    fun DropDown() { // dropdown menu
        Column {
            Row (modifier = Modifier.clickable { isExpanded.value = true }){
                Text(text = iKnowYouViewModel.theme.toString(), /*color = Color.Black*/)//MaterialTheme.colorScheme.onPrimary)
                Icon(Icons.Default.ArrowDropDown, contentDescription = null)
            }
        }
    }
    Scaffold (
        containerColor = MaterialTheme.colorScheme.background,
        modifier =
        Modifier.fillMaxSize().padding(top = 125.dp)
    ) { contentPadding ->

        Column( // column of buttons that change the background color of each screen
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(top = 100.dp)
                .fillMaxWidth()
        ) {
            Text(
                "Background color:",
                //color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.titleLarge
            )
            Box {
                DropDown()
                DropdownMenu(
                    expanded = isExpanded.value,
                    onDismissRequest = { isExpanded.value = false }) {
                    AppTheme.values().forEach { theme ->
                        DropdownMenuItem(
                            text = {
                                Text(text = theme.name, /*color = Color.Black*/)
                            },
                            onClick = {
                                iKnowYouViewModel.setTheme(theme)
                                isExpanded.value = false
                            }/*{
                                isExpanded.value = false

                                when(color){
                                    "Default" -> {
                                        iKnowYouViewModel.theme = AppTheme.SYSTEM
                                        iKnowYouViewModel.changeColor(defaultColor)
                                    }
                                    "Light" -> {
                                        iKnowYouViewModel.changeColor(Color.White)
                                        iKnowYouViewModel.theme = AppTheme.LIGHT
                                    }
                                    "Dark" -> {
                                        iKnowYouViewModel.changeColor(Color.Black)
                                        iKnowYouViewModel.theme = AppTheme.DARK
                                    }
                                    "Yellow" -> {
                                        iKnowYouViewModel.changeColor(Color.Yellow)
                                        iKnowYouViewModel.theme = AppTheme.SEPIA
                                    }
                                    "Purple" -> {
                                        iKnowYouViewModel.changeColor(Color.Magenta)
                                    }
                                    "Green" -> {
                                        iKnowYouViewModel.changeColor(Color.Green)
                                    }
                                }
                                iKnowYouViewModel.dropDownPosition = index
                            }*/
                        )

                    }
                }
            }
        }
    }
}