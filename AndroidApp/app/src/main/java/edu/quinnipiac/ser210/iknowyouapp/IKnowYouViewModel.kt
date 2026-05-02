package edu.quinnipiac.ser210.iknowyouapp

import android.R

import android.bluetooth.BluetoothSocket
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import edu.quinnipiac.ser210.iknowyouapp.connection.BluetoothHelper
import edu.quinnipiac.ser210.iknowyouapp.connection.Person
import edu.quinnipiac.ser210.iknowyouapp.ui.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class IKnowYouViewModel : ViewModel() {
    var backgroundColor: Color by mutableStateOf(Color.White)
    var dropDownPosition by mutableStateOf(0)
    var numScanned by mutableStateOf(2f)
    var possibleEnabled by mutableStateOf(true)
    var displayEnabled by mutableStateOf(true)
    var uiTransparency by mutableStateOf(1f)
    var aiPrompt by mutableStateOf("") //change to default if needed
    var fontScale by mutableStateOf(.55f)
    var paired by mutableStateOf(false)
    var btSocket by mutableStateOf<BluetoothSocket?>(null)
    //var theme by mutableStateOf(AppTheme.SYSTEM)
    var autoExpose by mutableStateOf(true)
    var exposure by mutableStateOf(7f)
    var personList by mutableStateOf(ArrayList<Person>())
    var people = mutableStateListOf<PersonCardData>()
        private set

    val _theme = MutableStateFlow(AppTheme.SYSTEM)
    val theme: StateFlow<AppTheme> = _theme
    //val BluetoothHelper by mutableStateOf()
    fun updateNumScanned(num: Float){
        numScanned = num
    }

    fun updatePossibleEnabled(isSwitched: Boolean){
        possibleEnabled = isSwitched
    }

    fun updateDisplayEnabled(isSwitched: Boolean){
        displayEnabled = isSwitched
    }

    fun updateTextAlpha(alpha: Float){
        uiTransparency = alpha
    }

    fun changeColor(color: Color) {
        this.backgroundColor = color
    }

    fun changePaired(paired: Boolean){
        this.paired = paired
    }

    fun changeSocket(socket: BluetoothSocket){
        this.btSocket = socket
    }
    fun upsertPerson(person: PersonCardData) {
        val existingIndex = people.indexOfFirst { it.id == person.id }

        if (existingIndex >= 0) {
            people[existingIndex] = person
        } else {
            people.add(0, person)
        }
    }

    fun setTheme(newTheme: AppTheme) {
        _theme.value = newTheme
    }


}