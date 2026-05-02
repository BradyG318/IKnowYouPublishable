package com.example.navigationdrawer.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.items
import androidx.navigation.NavHostController
import edu.quinnipiac.ser210.iknowyouapp.IKnowYouViewModel
import edu.quinnipiac.ser210.iknowyouapp.navigation.AppScreens
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import edu.quinnipiac.ser210.iknowyouapp.PersonCardData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DataScreen(iKnowYouViewModel: IKnowYouViewModel, navController: NavHostController) {
    val currPeople = iKnowYouViewModel.people

    Scaffold(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ) { contentPadding ->
        Text("Identified people will start to appear here:", modifier = Modifier.padding(top = 100.dp, start = 20.dp))
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 150.dp)
                .background(color = MaterialTheme.colorScheme.inversePrimary)
        ) {
            items(currPeople) { person ->
                DataCard(person) {
                    navController.navigate(route = AppScreens.DetailedDataScreen.name + "/${person.id}")
                }
            }
        }
    }
}
@Composable
fun DataCard(person: PersonCardData, action: () -> Unit) {
    val bitmap = BitmapFactory.decodeByteArray(
        person.imageBytes,
        0,
        person.imageBytes.size
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .clickable(onClick = action)
    ) {
        Row(
            modifier = Modifier.padding(12.dp)
        ) {
            if (bitmap != null) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = person.name,
                    modifier = Modifier.size(72.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(text = person.name, fontSize = 20.sp)
                Text(text = "Age: ${person.age}")
                Text(text = "ID: ${person.id}")
            }
        }
    }
}

@Composable
fun DataItem(person: PersonData, action: () -> Unit){
    Row (
        Modifier.fillMaxWidth()
            .clickable(onClick = action)
    ){
        Icon(Icons.Default.Person,contentDescription = null,Modifier.size(50.dp))
        Text(person.name, fontSize = 20.sp)
    }
}

data class PersonData(
    val id: Int,
    val name: String,
    val age: Int
)
