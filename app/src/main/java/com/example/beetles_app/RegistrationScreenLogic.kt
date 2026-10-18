package com.example.beetles_app

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.beetles_app.data.User
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object RegistrationScreenLogic {
    var selectedUser by mutableStateOf<User?>(null)

    var state by mutableStateOf(UserData())
        private set
    var courseMenuExpanded by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    fun onFirstNameChange(v: String) { state = state.copy(firstName = v); errorMessage = null }
    fun onLastNameChange(v: String)  { state = state.copy(lastName = v); errorMessage = null }
    fun onGenderChange(v: String)    { state = state.copy(gender = v) }
    fun onCourseChange(v: String) {
        state = state.copy(course = v)
        courseMenuExpanded = false
    }
    fun onDateChange(v: String) { state = state.copy(dateOfBirth = v); errorMessage = null }

    fun openCourseMenu()  { courseMenuExpanded = true }
    fun closeCourseMenu() { courseMenuExpanded = false }

    fun formatDate(millis: Long): String =
        SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(Date(millis))

    fun isFormFilled(): Boolean {
        return state.firstName.isNotBlank() &&
                state.lastName.isNotBlank() &&
                state.gender.isNotBlank() &&
                state.course.isNotBlank() &&
                state.dateOfBirth.isNotBlank()
    }

    fun clearForm() {
        state = UserData()
        errorMessage = null
    }

    fun setError(msg: String) { errorMessage = msg }

    fun selectUser(user: User) {
        selectedUser = user
        SettingsLogic.updateSettings(
            speed = user.speed,
            maxCockroaches = user.maxCockroaches,
            bonusInterval = user.bonusInterval,
            roundDuration = user.roundDuration
        )
    }
}