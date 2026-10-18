package com.example.beetles_app

import com.example.beetles_app.data.ScoreRepository

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beetles_app.data.AppDatabase
import kotlinx.coroutines.launch

@Composable
fun ScoresScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val repository = remember {
        ScoreRepository(AppDatabase.getDatabase(context).scoreDao())
    }
    val scope = rememberCoroutineScope()

    val scores by repository.getAllScores().collectAsState(initial = emptyList())

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Records",
            fontSize = 24.sp,
            color = Color(0xFFFDFDFD),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn {
            items(scores) { score ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1c242e))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "${score.firstName} ${score.lastName}",
                            color = Color(0xFFFDFDFD),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Score: ${score.score}",
                            color = Color(0xFF9BB0C4),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )

                        Spacer(modifier = Modifier.padding(top = 8.dp))

                        Text("Hits: ${score.hits}", color = Color(0xFFFDFDFD), fontSize = 14.sp)
                        Text("Misses: ${score.misses}", color = Color(0xFFFDFDFD), fontSize = 14.sp)
                        Text("Speed: ${score.speed}x", color = Color(0xFFFDFDFD), fontSize = 14.sp)

                        val accuracy = if (score.hits + score.misses > 0) {
                            score.hits * 100 / (score.hits + score.misses)
                        } else 0
                        Text("Accuracy: $accuracy%", color = Color(0xFF9BB0C4), fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
