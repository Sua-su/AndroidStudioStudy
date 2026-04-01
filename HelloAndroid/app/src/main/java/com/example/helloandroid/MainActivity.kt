package com.example.helloandroid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.helloandroid.ui.theme.HelloAndroidTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HelloAndroidTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RPSGame(modifier = Modifier.padding(innerPadding))
                }
            }
        }
        }
    }




@Composable
fun RPSGame(modifier: Modifier = Modifier) {
    var resultText by remember { mutableStateOf("시작") }
    var userChoice by remember { mutableStateOf("") }
    var computerChoice by remember { mutableStateOf("") }

    val choices = listOf("가위", "바위", "보")

    fun play(userPick: Int) {
        val computerPick = Random.nextInt(3)
        userChoice = choices[userPick]
        computerChoice = choices[computerPick]

        val result = when {
            userPick == computerPick -> "무승부"
            (userPick - computerPick + 3) % 3 == 1 -> "승리"
            else -> "패배"
        }
        resultText = "사용자: $userChoice | 상대 : $computerChoice\n결과: $result"
    }




    Column(
        modifier = modifier.fillMaxSize().padding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = resultText,
            fontSize = 20.sp,
            modifier = Modifier.padding(8.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Button(onClick = { play(0) }) {
                Text("가위")
            }
            Button(onClick = { play(1) }) {
                Text("바위")
            }
            Button(onClick = { play( userPick = 2)}) {
                Text("보")
            }
            }
        }
    }


@Preview(showBackground = true)
@Composable
fun RPSGamePreview() {
    HelloAndroidTheme {
        RPSGame()
    }
}
