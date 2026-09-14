package com.example.beetles_app

import android.R
import android.R.attr.top
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll


@Composable
fun RegistrationText(modifier: Modifier = Modifier) {
    var userName by remember { mutableStateOf("") }
    var userLastName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Man") }
    var selectedCourse by remember { mutableStateOf("Nothing") }
    var expanded by remember { mutableStateOf(false) }
    var difficulty by remember { mutableStateOf(1f) }
    var dateOfBirth by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var savedUser by remember { mutableStateOf<UserData?>(null) }
    val courses = listOf("1", "2", "3", "4")

    Column(
        modifier = modifier.fillMaxWidth()
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Registration user",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Serif,
            color = Color(0xFFFDFDFD),
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Card(
            modifier = Modifier.fillMaxWidth().padding(25.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1c242e)
            ),
            shape = RoundedCornerShape(30.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth().padding(top = 25.dp, bottom = 25.dp)
            ) {
                TextField(
                    value = userName,
                    onValueChange = { newText -> userName = newText },
                    label = { Text("Input the name") },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 25.dp),
                )

                TextField(
                    value = userLastName,
                    onValueChange = { newText -> userLastName = newText },
                    label = { Text("Input the last name") },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().padding(start = 25.dp, end = 25.dp, top = 25.dp),
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 25.dp, end = 25.dp, top = 25.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = gender == "Man",
                            onClick = { gender = "Man" },
                        )
                        Text(text = "Man", color = Color(0xFFFDFDFD))
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(
                            selected = gender == "Woman",
                            onClick = { gender = "Woman" },
                        )
                        Text(text = "Woman", color = Color(0xFFFDFDFD))
                    }
                }

                Box {
                    Button(
                        onClick = { expanded = true },
                        modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp)
                    ) {
                        Text("Choice the course")
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        courses.forEach { course ->
                            DropdownMenuItem(
                                text = { Text(course) },
                                onClick = {
                                    selectedCourse = course
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                Text(
                    text = "Select the game difficulty",
                    color = Color(0xFF9BB0C4),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 25.dp, top = 25.dp),
                )
                Slider(
                    value = difficulty,
                    onValueChange = { difficulty = it.toInt().toFloat() },
                    valueRange = 1f..10f,
                    steps = 8,
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 25.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Easy", color = Color(0xFF9BB0C4), fontSize = 12.sp)
                    Text(text = difficulty.toString(), color = Color(0xFFFDFDFD))
                    Text(text = "Hard", color = Color(0xFF9BB0C4), fontSize = 12.sp)
                }

                Text(
                    text = "Date of birth",
                    color = Color(0xFF9BB0C4),
                    modifier = Modifier.padding(start = 25.dp, top = 25.dp),
                    fontSize = 14.sp
                )
                Button(
                    onClick = { showDatePicker = true },
                    modifier = Modifier.fillMaxWidth().padding(start = 30.dp, end = 30.dp)
                ) {
                    Text(if (dateOfBirth.isEmpty()) "Select the date" else dateOfBirth)
                }

                if (showDatePicker) {
                    val datePickerState = rememberDatePickerState()
                    DatePickerDialog(
                        onDismissRequest = { showDatePicker = false },
                        confirmButton = {
                            TextButton(onClick = {
                                datePickerState.selectedDateMillis?.let {
                                    val format = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                                    dateOfBirth = format.format(Date(it))
                                }
                                showDatePicker = false
                            }) { Text("OK") }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDatePicker = false }) { Text("Cancel") }
                        }
                    ) {
                        DatePicker(state = datePickerState)
                    }
                }
                Button(
                    onClick = {
                        savedUser = UserData(
                            firstName = userName,
                            lastName = userLastName,
                            gender = gender,
                            course = selectedCourse,
                            difficulty = difficulty,
                            dateOfBirth = dateOfBirth,
                            zodiacSign = getZodiacSign(dateOfBirth)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 30.dp, end = 30.dp, top = 25.dp)
                ) {
                    Text("Save")
                }

                // ===== OUTPUT =====
                savedUser?.let { user ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(25.dp)
                    ) {
                        Text(text = "First name: ${user.firstName}", color = Color(0xFFFDFDFD))
                        Text(text = "Last name: ${user.lastName}", color = Color(0xFFFDFDFD))
                        Text(text = "Gender: ${user.gender}", color = Color(0xFFFDFDFD))
                        Text(text = "Course: ${user.course}", color = Color(0xFFFDFDFD))
                        Text(text = "Difficulty: ${user.difficulty}", color = Color(0xFFFDFDFD))
                        Text(text = "Date of birth: ${user.dateOfBirth}", color = Color(0xFFFDFDFD))
                        Text(
                            text = "Zodiac sign: ${user.zodiacSign}",
                            color = Color(0xFFFDFDFD),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}