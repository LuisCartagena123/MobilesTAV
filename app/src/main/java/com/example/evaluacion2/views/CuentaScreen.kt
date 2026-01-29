package com.example.evaluacion2.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.evaluacion2.viewmodel.AppViewModel
import com.example.evaluacion2.ui.theme.Dimens
import com.example.evaluacion2.ui.components.AppTopBar
import androidx.compose.material3.MaterialTheme

@Composable
fun CuentaScreen(navController: NavHostController, appViewModel: AppViewModel) {
    val usuarioActual = appViewModel.usuarioActual.collectAsState()

    Column {
        AppTopBar(title = "Mi Cuenta")
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(Dimens.screenPadding),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { Spacer(modifier = Modifier.height(Dimens.itemSpacing)) }

            usuarioActual.value?.let { usuario ->
                item {
                    // Avatar placeholder
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = usuario.email.first().uppercase(),
                            style = MaterialTheme.typography.displaySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(Dimens.itemSpacing)) }

                item {
                    // Email
                    Text(
                        text = "Email: ${usuario.email}",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                item { Spacer(modifier = Modifier.height(Dimens.itemSpacing)) }

                item {
                    // Logout button
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
}
