package com.example.beetles_app

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.random.Random

fun randomBug(id: Long): Bug {
    val type = when (Random.nextInt(10)) {
        0 -> BugType.RARE
        in 1..3 -> BugType.FAST
        else -> BugType.NORMAL
    }
    return Bug(
        id = id,
        x = Random.nextFloat(),
        y = Random.nextFloat(),
        vx = type.defaultSpeed * (if (Random.nextBoolean()) 1f else -1f),
        vy = type.defaultSpeed * (if (Random.nextBoolean()) 1f else -1f),
        size = type.defaultSize,
        type = type
    )
}

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    onExit: () -> Unit = {}
) {
    val roundDuration = SettingsLogic.state.roundDuration
    val maxBeetles = SettingsLogic.state.maxCockroaches
    val gameSpeed = SettingsLogic.state.speed

    var timeLeft by remember { mutableStateOf(roundDuration) }
    var showDialog by remember { mutableStateOf(false) }
    var roundKey by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var hits by remember { mutableStateOf(0) }
    var misses by remember { mutableStateOf(0) }

    val bugs = remember { mutableStateListOf<Bug>() }

    LaunchedEffect(roundKey) {
        bugs.clear()
        timeLeft = roundDuration
        showDialog = false
        score = 0
        hits = 0
        misses = 0

        repeat(maxBeetles) { i ->
            bugs.add(randomBug(i.toLong() + roundKey * 1000L))
        }

        launch {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft -= 1
            }
            showDialog = true
        }

        while (true) {
            delay(16)
            bugs.forEachIndexed { index, bug ->
                var x = bug.x + bug.vx * gameSpeed
                var y = bug.y + bug.vy * gameSpeed
                var vx = bug.vx
                var vy = bug.vy

                val half = bug.size / 2f
                val minX = half
                val maxX = 1f - half
                val minY = half
                val maxY = 1f - half

                if (x < minX) { x = minX; vx = abs(vx) }
                if (x > maxX) { x = maxX; vx = -abs(vx) }
                if (y < minY) { y = minY; vy = abs(vy) }
                if (y > maxY) { y = maxY; vy = -abs(vy) }

                bugs[index] = bug.copy(x = x, y = y, vx = vx, vy = vy)
            }
        }
    }

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val widthPx = constraints.maxWidth.toFloat()
        val heightPx = constraints.maxHeight.toFloat()
        val density = LocalDensity.current

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF25344a))
                .pointerInput(roundKey) {
                    detectTapGestures(onTap = {
                        misses += 1
                    })
                }
        ) {
            bugs.forEach { bug ->
                val sizePx = bug.size * widthPx

                with(density) {
                    Box(
                        modifier = Modifier
                            .offset(
                                x = (bug.x * widthPx - sizePx / 2f).toDp(),
                                y = (bug.y * heightPx - sizePx / 2f).toDp()
                            )
                            .size(sizePx.toDp())
                            .clip(CircleShape)
                            .background(bug.type.color)
                            .clickable {
                                score += bug.type.score
                                hits += 1
                                bugs.remove(bug)
                                bugs.add(randomBug(System.currentTimeMillis() + bug.id))
                            }
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Beetles",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    color = Color(0xFFFDFDFD),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "apocalypse",
                    fontSize = 18.sp,
                    color = Color(0xFF9BB0C4),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Left: $timeLeft sec",
                    fontSize = 20.sp,
                    color = Color(0xFFFDFDFD),
                    fontFamily = FontFamily.Serif
                )
                Text(
                    text = "Score: $score",
                    fontSize = 20.sp,
                    color = Color(0xFFFDFDFD),
                    fontFamily = FontFamily.Serif
                )
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { },
            title = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = "Round over", textAlign = TextAlign.Center)
                }
            },
            confirmButton = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Button(onClick = onExit) { Text("Exit") }
                    Button(onClick = { roundKey += 1 }) { Text("Repeat") }
                }
            }
        )
    }
}