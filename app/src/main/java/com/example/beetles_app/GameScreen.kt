package com.example.beetles_app

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.beetles_app.data.AppDatabase
import com.example.beetles_app.data.Score
import com.example.beetles_app.data.ScoreRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.sqrt
import kotlin.random.Random
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.media.AudioAttributes
import android.media.SoundPool
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableFloatStateOf

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

fun randomBugAwayFromOthers(existing: List<Bug>, id: Long): Bug {
    var newBug = randomBug(id)
    var attempts = 0
    while (attempts < 20) {
        val tooClose = existing.any { other ->
            val dx = other.x - newBug.x
            val dy = other.y - newBug.y
            val dist = sqrt(dx * dx + dy * dy)
            dist < (other.size + newBug.size)
        }
        if (!tooClose) break
        newBug = randomBug(id + attempts)
        attempts += 1
    }
    return newBug
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

    val bonuses = remember { mutableStateListOf<Bonus>() }

    val context = LocalContext.current
    val repository = remember {
        ScoreRepository(AppDatabase.getDatabase(context).scoreDao())
    }
    val scope = rememberCoroutineScope()

    val sensorManager = remember { context.getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    val gyroscope = remember { sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) }

    var gyroX by remember { mutableFloatStateOf(0f) }
    var gyroY by remember { mutableFloatStateOf(0f) }
    var tiltModeActive by remember { mutableStateOf(false) }

    DisposableEffect(gyroscope) {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                if (event.sensor.type == Sensor.TYPE_GYROSCOPE) {
                    gyroX = event.values[0]
                    gyroY = event.values[1]
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }
        if (gyroscope != null) {
            sensorManager.registerListener(listener, gyroscope, SensorManager.SENSOR_DELAY_GAME)
        }
        onDispose {
            sensorManager.unregisterListener(listener)
        }
    }

    val soundPool = remember {
        SoundPool.Builder()
            .setMaxStreams(1)
            .setAudioAttributes(
                AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_GAME)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
            )
            .build()
    }
    val screamSoundId = remember { soundPool.load(context, R.raw.bug_scream, 1) }

    DisposableEffect(Unit) {
        onDispose { soundPool.release() }
    }

    LaunchedEffect(roundKey) {
        bugs.clear()
        bonuses.clear()
        timeLeft = roundDuration
        showDialog = false
        score = 0
        hits = 0
        misses = 0

        repeat(maxBeetles) { i ->
            bugs.add(randomBugAwayFromOthers(bugs, i.toLong() + roundKey * 1000L))
        }

        launch {
            while (timeLeft > 0) {
                delay(1000)
                timeLeft -= 1
            }
            showDialog = true

            val currentUser = RegistrationScreenLogic.selectedUser
            if (currentUser != null) {
                val currentSpeed = SettingsLogic.state.speed
                scope.launch {
                    repository.saveBestScore(
                        Score(
                            firstName = currentUser.firstName,
                            lastName = currentUser.lastName,
                            score = score,
                            speed = currentSpeed,
                            hits = hits,
                            misses = misses
                        )
                    )
                }
            }
        }

        launch {
            while (true) {
                delay(15_000L)
                bonuses.clear()
                bonuses.add(
                    Bonus(
                        id = System.currentTimeMillis(),
                        x = Random.nextFloat(),
                        y = Random.nextFloat()
                    )
                )
            }
        }

        while (true) {
            delay(16)
            bugs.forEachIndexed { index, bug ->
                var x: Float
                var y: Float
                var vx = bug.vx
                var vy = bug.vy

                if (tiltModeActive) {
                    x = bug.x + gyroY * 0.02f
                    y = bug.y + gyroX * 0.02f
                    vx = 0f
                    vy = 0f
                } else {
                    x = bug.x + bug.vx * gameSpeed
                    y = bug.y + bug.vy * gameSpeed
                }

                val half = bug.size / 2f
                val minX = half
                val maxX = 1f - half
                val minY = half
                val maxY = 1f - half

                if (x < minX) { x = minX; if (!tiltModeActive) vx = abs(vx) }
                if (x > maxX) { x = maxX; if (!tiltModeActive) vx = -abs(vx) }
                if (y < minY) { y = minY; if (!tiltModeActive) vy = abs(vy) }
                if (y > maxY) { y = maxY; if (!tiltModeActive) vy = -abs(vy) }

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
                    detectTapGestures(onTap = { offset ->

                        var tappedBonus: Bonus? = null
                        for (bonus in bonuses) {
                            val sizePx = bonus.size * widthPx
                            val centerX = bonus.x * widthPx
                            val centerY = bonus.y * heightPx
                            val radius = sizePx / 2f

                            val dx = offset.x - centerX
                            val dy = offset.y - centerY
                            if (dx * dx + dy * dy < radius * radius) {
                                tappedBonus = bonus
                                break
                            }
                        }

                        var tapped: Bug? = null

                        for (bug in bugs) {
                            val sizePx = bug.size * widthPx
                            val centerX = bug.x * widthPx
                            val centerY = bug.y * heightPx
                            val radius = sizePx / 2f

                            val dx = offset.x - centerX
                            val dy = offset.y - centerY
                            if (dx * dx + dy * dy < radius * radius) {
                                tapped = bug
                                break
                            }
                        }

                        val target = tapped
                        if (tappedBonus != null) {
                            bonuses.remove(tappedBonus)
                            tiltModeActive = true
                            soundPool.play(screamSoundId, 1f, 1f, 1, 0, 1f)
                            scope.launch {
                                delay(15000)
                                tiltModeActive = false
                            }
                        } else if (target != null) {
                            score += target.type.score
                            hits += 1
                            bugs.remove(target)
                            bugs.add(
                                randomBugAwayFromOthers(
                                    bugs,
                                    System.currentTimeMillis() + target.id
                                )
                            )
                        } else {
                            misses += 1
                        }
                    })
                }
        ) {
            bugs.forEach { bug ->
                val sizePx = bug.size * widthPx

                with(density) {
                    val angle = Math.toDegrees(
                        atan2(bug.vy.toDouble(), bug.vx.toDouble())
                    ).toFloat() + 90f

                    Image(
                        painter = painterResource(id = bug.type.imageRes),
                        contentDescription = bug.type.name,
                        modifier = Modifier
                            .offset(
                                x = (bug.x * widthPx - sizePx / 2f).toDp(),
                                y = (bug.y * heightPx - sizePx / 2f).toDp()
                            )
                            .size(sizePx.toDp())
                            .rotate(angle)
                    )
                }
            }

            bonuses.forEach { bonus ->
                val sizePx = bonus.size * widthPx
                with(density) {
                    Image(
                        painter = painterResource(id = R.drawable.bonus),
                        contentDescription = "Bonus",
                        modifier = Modifier
                            .offset(
                                x = (bonus.x * widthPx - sizePx / 2f).toDp(),
                                y = (bonus.y * heightPx - sizePx / 2f).toDp()
                            )
                            .size(sizePx.toDp())
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
                Text(
                    text = "Hits: $hits | Misses: $misses",
                    fontSize = 14.sp,
                    color = Color(0xFF9BB0C4)
                )
            }
        }
    }

    if (showDialog) {
        val accuracy = if (hits + misses > 0) hits * 100 / (hits + misses) else 0

        AlertDialog(
            onDismissRequest = { },
            title = {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(text = "Round over", textAlign = TextAlign.Center)
                }
            },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "Score: $score", fontSize = 16.sp)
                    Text(text = "Hits: $hits", fontSize = 16.sp)
                    Text(text = "Misses: $misses", fontSize = 16.sp)
                    Text(text = "Accuracy: $accuracy%", fontSize = 16.sp)
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