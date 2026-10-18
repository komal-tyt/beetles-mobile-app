package com.example.beetles_app

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beetles_app.data.AppDatabase

@Composable
fun UsersListScreen(
    onPlay: () -> Unit,
    onCreateNew: () -> Unit
) {
    val context = LocalContext.current
    val usersFlow = AppDatabase.getDatabase(context).userDao().getAllUsers()
    val users by usersFlow.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Select User",
            fontSize = 24.sp,
            color = Color(0xFFFDFDFD),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(users) { user ->
                val isSelected = RegistrationScreenLogic.selectedUser?.id == user.id
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .then(
                            if (isSelected) Modifier.border(2.dp, Color(0xFF4CAF50), RoundedCornerShape(12.dp))
                            else Modifier
                        )
                        .clickable {
                            RegistrationScreenLogic.selectUser(user)
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) Color(0xFF2A3B4C) else Color(0xFF1c242e)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${user.firstName} ${user.lastName}",
                            color = Color(0xFFFDFDFD),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Date of birth: ${user.dateOfBirth}",
                            color = Color(0xFF9BB0C4),
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Button(
                onClick = onPlay,
                modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                enabled = RegistrationScreenLogic.selectedUser != null,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
            ) {
                Text("Play Selected User", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                onClick = onCreateNew,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50))
            ) {
                Text("Register New User", fontSize = 16.sp)
            }
        }
    }
}