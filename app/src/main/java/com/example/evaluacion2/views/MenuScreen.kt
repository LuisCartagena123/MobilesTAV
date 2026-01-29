package com.example.evaluacion2.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.evaluacion2.viewmodel.AppViewModel
import com.example.evaluacion2.ui.theme.Dimens
import com.example.evaluacion2.ui.components.AppTopBar

@Composable
fun MenuScreen(navController: NavHostController, appViewModel: AppViewModel) {
    val usuarioActual = appViewModel.usuarioActual.collectAsState()
    val esAdmin = usuarioActual.value?.isAdmin ?: false
    
    Column {
        AppTopBar(title = "Menú")
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.screenPadding),
            verticalArrangement = Arrangement.Top
        ) {
            item { Spacer(Modifier.height(Dimens.itemSpacing)) }
            
            // Botón Agregar Libro solo para admins
            if (esAdmin) {
                item {
                    Row {
                        Button(onClick = { navController.navigate("addBook") }) {
                            Text("Agregar libro")
                        }
                        Spacer(Modifier.width(Dimens.itemSpacing))
                        Button(onClick = { navController.navigate("adminUsers") }) {
                            Text("Administrar usuarios")
                        }
                    }
                }
                item { Spacer(Modifier.height(Dimens.itemSpacing)) }
            }
            
            // Botón Cerrar sesión
            item {
                Button(onClick = {
                    appViewModel.logout()
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }) {
                    Text("Cerrar sesión")
                }
            }
        }
    }
}
