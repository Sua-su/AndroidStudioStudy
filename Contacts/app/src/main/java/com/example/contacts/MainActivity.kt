package com.example.contacts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.contacts.navigation.ContactsApp
import com.example.contacts.navigation.MainScreen
import com.example.contacts.ui.MainViewModel
import com.example.contacts.ui.theme.ContactsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val viewModel: MainViewModel by viewModels ()
        setContent {
            ContactsTheme {
                ContactsApp(viewModel)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val keyword="Hello"
    ContactsTheme {
        MainScreen(keyword, {it})
    }
}