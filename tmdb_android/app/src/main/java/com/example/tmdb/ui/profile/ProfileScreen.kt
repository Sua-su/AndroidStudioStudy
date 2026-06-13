package com.example.tmdb.ui.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tmdb.model.Achievement

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: ProfileViewModel = hiltViewModel()
) {
    val nickname by viewModel.nickname.collectAsState()
    var tempNickname by remember { mutableStateOf(nickname ?: "") }
    val achievements by viewModel.achievements.collectAsState()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text("Profile Settings") })
        }
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (nickname != null) "Welcome, $nickname!" else "You are browsing anonymously",
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                "My Badges",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp)
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier.height(150.dp).padding(vertical = 8.dp)
            ) {
                items(achievements) { achievement ->
                    BadgeItem(achievement)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = tempNickname,
                onValueChange = { tempNickname = it },
                label = { Text("Enter Nickname") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.setNickname(tempNickname) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Nickname")
            }
            if (nickname != null) {
                TextButton(
                    onClick = {
                        viewModel.logout()
                        tempNickname = ""
                    },
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Logout (Go Anonymous)")
                }
            }
        }
    }
}

@Composable
fun BadgeItem(achievement: Achievement) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(4.dp)
    ) {
        val color = if (achievement.isUnlocked) MaterialTheme.colorScheme.primary else Color.Gray
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = color.copy(alpha = 0.1f),
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = if (achievement.isUnlocked) "🏆" else "🔒",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
        Text(
            text = achievement.name,
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}
