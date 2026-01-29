package com.example.evaluacion2.views

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.evaluacion2.viewmodel.AppViewModel

@Composable
fun AppNavigation(navController: NavHostController, appViewModel: AppViewModel) {
	NavHost(navController = navController, startDestination = "login") {
		composable("login") { LoginScreen(navController, appViewModel) }
		composable("registro") { RegistroScreen(navController, appViewModel) }
		composable("home") { MainScreen(navController, appViewModel) }
		composable("addBook") { AddBookScreen(navController, appViewModel) }
		composable("cuenta") { CuentaScreen(navController, appViewModel) }
		composable("adminUsers") { AdminUsersScreen(navController, appViewModel) }
	}
}
