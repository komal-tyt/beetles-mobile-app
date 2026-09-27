package com.example.beetles_app

import android.widget.TextView
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
fun RulesScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current

    val html = context.resources.openRawResource(R.raw.rules).bufferedReader().use { it.readText() }

    AndroidView(
        modifier = modifier.fillMaxSize().padding(16.dp),
        factory = { ctx ->
            TextView(ctx).apply {
                text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_LEGACY)
                textSize = 16f
                setTextColor(android.graphics.Color.WHITE)
                setBackgroundColor(android.graphics.Color.parseColor("#25344a"))
            }
        }
    )
}