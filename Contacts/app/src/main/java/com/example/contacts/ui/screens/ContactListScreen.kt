package com.example.contacts.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.contacts.data.Contact
import com.example.contacts.ui.components.ContactListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactListScreen(contacts: List<Contact>, onContactClick: (Int) -> Unit) {

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
        val query = remember { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            TextField(
                value = query.value,
                placeholder = { Text("enter keyword") },
                onValueChange = { query.value = it },
                modifier = Modifier.fillMaxWidth()
            )
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                val filteredContacts = if (query.value.isEmpty()) {
                    contacts
                } else {
                    contacts.filter { it.name.contains(query.value, ignoreCase = true) }
                }
                items(filteredContacts) { contact ->
                    ContactListItem(contact, onContactClick)
                }
            }
        }
    }
}