package com.example.contacts.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.contacts.data.getContacts
import com.example.contacts.ui.screens.ContactDetailScreen
import com.example.contacts.ui.screens.ContactListScreen
import kotlinx.serialization.Serializable

@Serializable
object ListRoute
@Serializable
data class DetailRoute(val id: Int)

@Composable
fun ContactsApp() {
    val navController = rememberNavController()
    val contacts = remember { getContacts() }
    NavHost(navController = navController, startDestination = ListRoute) {

// --- [목록 화면] ---
        composable<ListRoute> {
            ContactListScreen(
                contacts,
                onContactClick = { id ->
                    navController.navigate(DetailRoute(id))
                }
            )
        }

// --- [상세 화면] ---
        composable<DetailRoute> { backStackEntry ->
            val routeData = backStackEntry.toRoute<DetailRoute>()
            val contact = contacts.find { it.id == routeData.id }
            ContactDetailScreen(
                contact!!,
                onBackClick = { navController.popBackStack() } // 뒤로 가기
            )
        }
    }
}