package com.example.coroutinepractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.coroutinepractice.ui.theme.CoroutinePracticeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeout
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel by viewModels()
        enableEdgeToEdge()
        setContent {
            CoroutinePracticeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Main(
                        modifier = Modifier.padding(innerPadding),
                        viewModel
                    )
                }
            }
        }
    }
}
@Composable
fun Main(modifier: Modifier= Modifier, viewModel: MainViewModel) {
    Column(modifier = modifier) {
        NowButton(viewModel.nowState.collectAsState().value, viewModel::updateNow)
        BlockingButton(viewModel.delayedState.collectAsState().value, viewModel::updateDelayed)
    }
}
        @Composable
        fun NowButton(now: String, onClick:()-> Unit){
            Button(onClick = onClick){
                Text(text = now.value)
            }
        }
        @Composable
        fun NowButton(now: String, onClick: () -> Unit){
            Button(onClick) {
                Text(text = now)
            }
        }

        @Composable
        fun BlockingButton(){
            val now = remember { mutableStateOf(LocalDateTime.now().toString()) }
            Button({
                CoroutineScope(Dispatchers.IO).launch {
                    delay(3000)

                    now.value = LocalDateTime.now().toString()
                }
            }){
                Text(text = now.value)
            }
        }
