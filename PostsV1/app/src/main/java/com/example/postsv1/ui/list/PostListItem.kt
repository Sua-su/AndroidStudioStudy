package com.example.postsv1.ui.list

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.postsv1.data.dto.PostDto

@Composable
fun PostListItem(post: PostDto, onPostClick: (Int) -> Unit){
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPostClick(post.id) }
    ) {
        Row(modifier = Modifier.padding(16.dp)) {
            Text( text = post.id.toString())
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = post.title,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}