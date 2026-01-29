package com.example.evaluacion2.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.evaluacion2.viewmodel.AppViewModel
import com.example.evaluacion2.ui.components.AppTopBar
import com.example.evaluacion2.ui.theme.Dimens

@Composable
fun AdminUsersScreen(navController: NavHostController, appViewModel: AppViewModel) {
    val usuariosList = appViewModel.usuarios.collectAsState()

    LaunchedEffect(Unit) {
        appViewModel.cargarUsuarios()
    }

    Column {
        AppTopBar(title = "Administración de Usuarios")
        Column(modifier = Modifier.padding(Dimens.screenPadding)) {
            TextButton(onClick = {
                navController.navigate("home") {
                    popUpTo("home") { inclusive = true }
                }
            }) {
                Text("Volver al inicio")
            }
            Spacer(modifier = Modifier.height(Dimens.itemSpacing))
            if (usuariosList.value.isEmpty()) {
                Text("No hay usuarios registrados", style = MaterialTheme.typography.bodyMedium)
            } else {
                LazyColumn {
                    itemsIndexed(usuariosList.value) { _, usuario ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(Dimens.itemSpacing),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = usuario.nombre,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                    Text(
                                        text = "@${usuario.username}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = usuario.email,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = if (usuario.isAdmin) "Admin" else "Cliente",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = if (usuario.isAdmin) 
                                            MaterialTheme.colorScheme.primary 
                                        else 
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Checkbox(
                                        checked = usuario.isAdmin,
                                        onCheckedChange = { nuevoValor ->
                                            appViewModel.cambiarIsAdmin(usuario.email, nuevoValor)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
