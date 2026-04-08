package com.example.contacts.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.contacts.R
import com.example.contacts.data.Contact

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactDetailScreen(contact: Contact, onBackClick: () -> Unit) {

// 이 화면이 '구성(Enter)'될 때 실행됨.
    LaunchedEffect(key1 = contact.name) {
// Logcat 출력
        Log.d("NavigationTest",

            "상세 화면 로딩 완료: ${contact.name}")

    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("상세 정보") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(painterResource(R.drawable.outline_arrow_back_24), contentDescription = "뒤로 가기")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp)
        ) {
            Text(text = "이름"

                , style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)

            Text(text = contact.name, style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "전화번호"

                , style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)

            Text(text = contact.personalPhoneNumber, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "회사 전화번호"

                , style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)

            Text(text = contact.officePhoneNumber, style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Email", style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.primary)
            Text(text = contact.email, style = MaterialTheme.typography.headlineSmall)
        }
    }
}