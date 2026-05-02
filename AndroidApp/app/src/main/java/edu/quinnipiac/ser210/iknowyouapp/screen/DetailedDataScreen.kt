package edu.quinnipiac.ser210.iknowyouapp.screen

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import edu.quinnipiac.ser210.iknowyouapp.IKnowYouViewModel

@Composable
fun DetainedDataScreen(
    iKnowYouViewModel: IKnowYouViewModel,
    navController: NavController,
    name: String
) {
    val person = iKnowYouViewModel.people.find { it.id.toString() == name }

    Scaffold(
        //containerColor = iKnowYouViewModel.backgroundColor
    ) { contentPadding ->
        Column(
            Modifier
                .fillMaxWidth()
                .padding(contentPadding)
                .padding(top = 150.dp, start = 16.dp, end = 16.dp)
        ) {
            if (person == null) {
                Text("Person not found")
            } else {
                val bitmap = BitmapFactory.decodeByteArray(
                    person.imageBytes,
                    0,
                    person.imageBytes.size
                )

                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = person.name,
                        modifier = Modifier.size(180.dp)
                    )
                }

                Text("Name: ${person.name}")
                Text("Age: ${person.age}")
                Text("ID: ${person.id}")
            }
        }
    }
}