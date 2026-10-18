package com.example.beetles_app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.launch

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onPlay: () -> Unit = {}
) {
    var showCreateForm by remember { mutableStateOf(false) }

    if (showCreateForm) {
        RegistrationScreen(
            onBack = { showCreateForm = false },
            onUserCreated = { showCreateForm = false }
        )
        return
    }

    val tabs = listOf("Users", "Settings", "Rules", "Authors", "Records")
    val pagerState = rememberPagerState(pageCount = { tabs.size })
    val scope = rememberCoroutineScope()

    Column(modifier = modifier.fillMaxSize()) {
        ScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent,
            contentColor = Color(0xFF9BB0C4),
            edgePadding = 16.dp,
            modifier = Modifier.padding(top = 50.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = {
                        scope.launch { pagerState.animateScrollToPage(index) }
                    },
                    text = { Text(title, fontSize = 20.sp) }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f).fillMaxWidth()
        ) { page ->
            when (page) {
                0 -> UsersListScreen(
                    onPlay = onPlay,
                    onCreateNew = { showCreateForm = true }
                )
                1 -> SettingsScreen()
                2 -> RulesScreen()
                3 -> AuthorsScreen()
                4 -> ScoresScreen()
            }
        }
    }
}