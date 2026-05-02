package edu.quinnipiac.ser210.iknowyouapp.connection

data class RecognizedPerson(
    val trackId: Int,
    val faceId: Int,
    val age: Int,
    val fullName: String,
    val imageBytes: ByteArray
)