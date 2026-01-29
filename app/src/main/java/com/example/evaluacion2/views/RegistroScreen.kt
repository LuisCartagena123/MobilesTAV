package com.example.evaluacion2.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.evaluacion2.viewmodel.AppViewModel
import com.example.evaluacion2.ui.theme.Dimens
import com.example.evaluacion2.ui.components.AppTopBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroScreen(navController: NavHostController, appViewModel: AppViewModel) {
    var nombre by remember { mutableStateOf("") }
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }
    
    // Monitorear cambios del usuario actual
    val usuarioActual by appViewModel.usuarioActual.collectAsState()
    
    LaunchedEffect(usuarioActual) {
        if (usuarioActual != null) {
            navController.navigate("home") {
                popUpTo("login") { inclusive = false }
            }
        }
    }

    Scaffold(topBar = { AppTopBar("Registro de Usuario") }) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.screenPadding),
            verticalArrangement = Arrangement.Center
        ) {
            item {
                TextButton(onClick = {
                    navController.navigate("login") {
                        popUpTo("login") { inclusive = true }
                    }
                }) {
                    Text("Volver")
                }
            }
            item { Spacer(Modifier.height(Dimens.itemSpacing)) }
            item {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = error.isNotEmpty()
                )
            }
            item { Spacer(Modifier.height(Dimens.itemSpacing)) }
            item {
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Usuario") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = error.isNotEmpty(),
                    leadingIcon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Usuario") }
                )
            }
            item { Spacer(Modifier.height(Dimens.itemSpacing)) }
            item {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = error.isNotEmpty(),
                    leadingIcon = { Icon(Icons.Filled.Email, contentDescription = "Email") }
                )
            }
            item { Spacer(Modifier.height(Dimens.itemSpacing)) }
            item {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = error.isNotEmpty(),
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Contraseña") },
                    visualTransformation = if (password.isNotEmpty()) PasswordVisualTransformation() else VisualTransformation.None
                )
            }
            item { Spacer(Modifier.height(Dimens.itemSpacing)) }
            item {
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirmar Contraseña") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = error.isNotEmpty(),
                    leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = "Confirmar Contraseña") },
                    visualTransformation = if (confirmPassword.isNotEmpty()) PasswordVisualTransformation() else VisualTransformation.None
                )
            }
            item { Spacer(Modifier.height(Dimens.sectionSpacing)) }
            item {
                AnimatedVisibility(visible = error.isNotEmpty()) {
                    Column {
                        Text(error, color = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.height(Dimens.itemSpacing))
                    }
                }
            }
            item {
                Button(
                    onClick = {
                        when {
                            nombre.isBlank() || username.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank() ->
                                error = "Todos los campos son Obligatorios!!"
                            !email.contains("@") || email.length < 6 ->
                                error = "email NO válido"
                            username.length < 3 ->
                                error = "Usuario mínimo de 3 caracteres"
                            password.length < 3 ->
                                error = "Contraseña mínimo de 3 caracteres"
                            password != confirmPassword ->
                                error = "Las contraseñas NO coinciden"
                            else -> appViewModel.registrarUsuario(email, username, nombre, password) { ok ->
                                if (!ok) {
                                    error = "El usuario YA Existe!!"
                                } else {
                                    error = ""
                                    navController.navigate("login")
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Registrarse")
                }
            }
        }
    }
}
