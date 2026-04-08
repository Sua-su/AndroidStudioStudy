package com.example.contacts.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsListScreen(contacts: List<Contact>, onContactsClick: (Int) -> Unit) {

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("my num") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { innerPadding ->
        val query = remeber { mutableStateOf("") }
        Column (modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Row  {
                TextField(
                    value = query.value,
                    placeholder = {Text("enter keyword")},
                    onValueChange = {query.value = it},
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(contacts) {contact ->
                ContactsListItem(contact, onContactClick)
            }
        }
      \

    }

}