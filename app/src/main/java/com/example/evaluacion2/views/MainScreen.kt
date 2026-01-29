 package com.example.evaluacion2.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Badge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.evaluacion2.viewmodel.AppViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.material3.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart

private data class BottomItem(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val bottomItems = listOf(
    BottomItem("inicio", "Inicio", Icons.Filled.Home),
    BottomItem("cuenta", "Cuenta", Icons.Filled.AccountCircle),
    BottomItem("carro", "Carro", Icons.Filled.ShoppingCart),
    BottomItem("menu", "Menu", Icons.Filled.Menu)
)

@Composable
fun MainScreen(navController: NavHostController, appViewModel: AppViewModel) {
    val tabsController = rememberNavController()
    val usuarioState by appViewModel.usuarioActual.collectAsState()
    val carroCount by appViewModel.carro.collectAsState()

    Scaffold(
        bottomBar = {
            val backStackEntry by tabsController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            NavigationBar(containerColor = MaterialTheme.colorScheme.primaryContainer) {
                bottomItems.forEach { item ->
                    NavigationBarItem(
                        selected = item.route == currentRoute,
                        onClick = {
                            if (currentRoute != item.route) {
                                tabsController.navigate(item.route) {
                                    popUpTo(tabsController.graph.startDestinationId) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            unselectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                            unselectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                            indicatorColor = MaterialTheme.colorScheme.primary
                        ),
                        icon = {
                            if (item.route == "carro") {
                                BadgedBox(
                                    badge = {
                                        if (carroCount.isNotEmpty()) {
                                            Badge {
                                                Text(carroCount.size.toString())
                                            }
                                        }
                                    }
                                ) {
                                    Icon(item.icon, contentDescription = item.label)
                                }
                            } else {
                                Icon(item.icon, contentDescription = item.label)
                            }
                        },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { _ ->
        NavHost(
            navController = tabsController,
            startDestination = "inicio"
        ) {
            composable("inicio") { LibrosScreen(navController, appViewModel) }
            composable("cuenta") { CuentaScreen(navController, appViewModel) }
            composable("carro") { CarroScreen(navController, appViewModel) }
            composable("menu") { MenuScreen(navController, appViewModel) }
        }
    }
}
