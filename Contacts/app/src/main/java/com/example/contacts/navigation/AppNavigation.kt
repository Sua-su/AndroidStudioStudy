package com.example.contacts.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.contacts.data.getContacts
import com.example.contacts.ui.MainViewModel
import com.example.contacts.ui.screens.ContactDetailScreen
import com.example.contacts.ui.screens.ContactListScreen
import kotlinx.serialization.Serializable

@Serializable
object ListRoute
@Serializable
data class DetailRoute(val id: Int)


@Composable
fun MainScreen(keyword: String, updateKeyword:(String)-> Unit){
    val navController = rememberNavController()
    val contacts = remember { getContacts() }

    NavHost(navController = navController, startDestination = ListRoute) {
        composable<ListRoute> {
            ContactListScreen(
                keyword,
                updateKeyword,
                contacts,
                onContactClick = { id ->
                    navController.navigate(DetailRoute(id))
                }
            )
        }
        composable<DetailRoute> { backStackEntry ->
            val routeData = backStackEntry.toRoute<DetailRoute>()
            val contact = contacts.find { it.id==routeData.id }
            ContactDetailScreen(
                contact!!,
                onBackClick = {navController.popBackStack()}
            )
        }
    }
}



@Composable
fun ContactsApp(ViewModel : MainViewModel ) {
    MainScreen(keyword = viewModel.keywordstate.collectAsState().value, updateKeyword = viewModel::updateKeyword)

}