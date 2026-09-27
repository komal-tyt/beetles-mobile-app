package com.example.beetles_app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegistrationScreenLogic {
    var state by mutableStateOf(UserData())
        private set

    var courseMenuExpanded by mutableStateOf(false)
        private set

    var savedUser by mutableStateOf<UserData?>(null)
        private set

    fun onFirstNameChange(v: String) { state = state.copy(firstName = v) }
    fun onLastNameChange(v: String)  { state = state.copy(lastName = v) }
    fun onGenderChange(v: String)    { state = state.copy(gender = v) }

    fun onCourseChange(v: String) {
        state = state.copy(course = v)
        courseMenuExpanded = false
    }

    fun onDifficultyChange(v: Float) { state = state.copy(difficulty = v) }
    fun onDateChange(v: String) { state = state.copy(dateOfBirth = v) }

    fun openCourseMenu()  { courseMenuExpanded = true }
    fun closeCourseMenu() { courseMenuExpanded = false }

    fun save() {
        val finalUser = state.copy(zodiacSign = getZodiacSign(state.dateOfBirth))
        state = finalUser
        savedUser = finalUser
    }

    fun formatDate(millis: Long): String =
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(millis))
}