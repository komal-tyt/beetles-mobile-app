package com.example.beetles_app

import android.R
import android.R.attr.top
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RegistrationText(modifier: Modifier = Modifier) {
    var userName by remember { mutableStateOf("") }
    var userLastName by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("Man") }
    Column(
        modifier = modifier.fillMaxWidth()
    ) { Text(
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
                modifier = Modifier.fillMaxWidth().padding(top = 25.dp)
            ) {
                TextField(
                    value = userName,
                    onValueChange = { newText -> userName = newText},
                    label = { Text("Input the name") },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 25.dp),
                )

                TextField(
                    value = userLastName,
                    onValueChange = { newText -> userLastName = newText},
                    label = { Text("Input the last name") },
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth().padding(start = 25.dp, end = 25.dp, top = 25.dp),
                )
                Row(
                    modifier = Modifier.fillMaxWidth().padding(start = 25.dp, end = 25.dp, top = 25.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = gender == "Man",
                            onClick = { gender = "Man" },
                        )
                        Text(
                            text = "Man",
                            color = Color(0xFFFDFDFD),
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = gender == "Woman",
                            onClick = { gender = "Woman" },
                        )
                        Text(
                            text = "Woman",
                            color = Color(0xFFFDFDFD),
                        )
                    }
                }
            }
        }
    }
}


