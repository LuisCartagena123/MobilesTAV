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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController, appViewModel: AppViewModel) {
    var identifier by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    Scaffold(topBar = { AppTopBar("Login") }) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.screenPadding),
            verticalArrangement = Arrangement.Center
        ) {
            item {
                OutlinedTextField(
                    value = identifier,
                    onValueChange = { identifier = it },
                    label = { Text("Email o Usuario") },
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
                        if (identifier.isBlank() || password.isBlank()) {
                            error = "Debe ingresar Usuario y Contraseña"
                        } else if (password.length < 3) {
                            error = "La contraseña debe tener al menos 3 caracteres"
                        } else {
                            appViewModel.login(identifier, password) { ok ->
                                if (ok) {
                                    error = ""
                                    navController.navigate("home") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                } else {
                                    error = "Usuario y/o Contraseña Incorrectos"
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ingresar")
                }
            }
            item { Spacer(Modifier.height(Dimens.itemSpacing)) }
            item {
                TextButton(onClick = { navController.navigate("registro") }, modifier = Modifier.fillMaxWidth()) {
                    Text("¿No tienes Cuenta?...Regístrate Aquí!")
                }
            }
        }
    }
}
