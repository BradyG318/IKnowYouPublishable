package com.example.navigationdrawer.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import edu.quinnipiac.ser210.iknowyouapp.IKnowYouViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(iKnowYouViewModel: IKnowYouViewModel) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Scaffold(

    ) { contentPadding ->
        Column (
            Modifier.fillMaxWidth()
                .padding(top = 150.dp)
        ) {
            Text("Login to use the app.")
            Text("Username:")
            TextField(
                value = username,
                onValueChange = {username = it},
                singleLine = true
            )
            Text("Password:")
            TextField(
                value = password,
                onValueChange = {password = it},
                singleLine = true
            )
        }
    }
}