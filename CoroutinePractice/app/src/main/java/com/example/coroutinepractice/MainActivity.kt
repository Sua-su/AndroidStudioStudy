package com.example.coroutinepractice

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.example.coroutinepractice.data.dto.PostDto
import com.example.coroutinepractice.ui.theme.CoroutinePracticeTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
fun Main(modifier: Modifier = Modifier, viewModel: MainViewModel) {
    Column(modifier = modifier){
        Posts(viewModel.postState.collectAsState().value, viewModel::fetchPosts)
    }
}

@Composable
fun Posts(posts:List<PostDto>, onClick:()->Unit){
    Column{
        Text(text = posts.size.toString())
        Button(onClick = onClick){
            Text(text = "fetch")
        }
    }
}
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CoroutinePracticeTheme {
    }
}