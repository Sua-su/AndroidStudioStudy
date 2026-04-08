package com.example.contacts.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.contacts.data.Contact

@Composable
fun ContactListItem(contact: Contact, onContactClick: (Int) -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable { onContactClick(contact.id) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = contact.name,
                modifier = Modifier.width(160.dp),
                style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.width(24.dp))
            Text(text = contact.personalPhoneNumber)
        }
    }
}