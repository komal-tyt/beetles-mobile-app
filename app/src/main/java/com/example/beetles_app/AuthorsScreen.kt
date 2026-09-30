package com.example.beetles_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Author(val name: String, val photoRes: Int)

@Composable
fun AuthorsScreen(modifier: Modifier = Modifier) {
    val authors = listOf(
        Author("Nail Gadirov", R.drawable.author1),
        Author("Dmitriy Kovalenko", R.drawable.author2)
    )

    LazyColumn(modifier = modifier.fillMaxWidth().padding(16.dp)) {
        items(authors) { author ->
            Card(
                modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1c242e))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(author.photoRes),
                        contentDescription = author.name,
                        modifier = Modifier.size(56.dp).clip(CircleShape)
                    )
                    Text(
                        text = author.name,
                        color = Color(0xFFFDFDFD),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                }
            }
        }
    }
}