package com.example.evaluacion2.views

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import androidx.compose.material3.MaterialTheme
import com.example.evaluacion2.ui.theme.Dimens
import com.example.evaluacion2.viewmodel.AppViewModel
import androidx.compose.foundation.text.KeyboardOptions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBookScreen(navController: NavHostController, appViewModel: AppViewModel) {
    var titulo by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    var paginasText by remember { mutableStateOf("") }
    var precioText by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var imagenUri by remember { mutableStateOf("") }
    var buscando by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    val pickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        uri?.let { imagenUri = it.toString() }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nuevo libro") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                navigationIcon = {
                    TextButton(onClick = { navController.popBackStack() }) { Text("Atrás") }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(Dimens.screenPadding),
            verticalArrangement = Arrangement.Top
        ) {
            item {
                OutlinedTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = error.isNotEmpty()
                )
            }
            item { Spacer(Modifier.height(Dimens.itemSpacing)) }
            item {
                Button(
                    onClick = {
                        if (titulo.isBlank()) {
                            error = "Ingresa un título para buscar"
                            return@Button
                        }
                        buscando = true
                        error = ""
                        appViewModel.buscarLibroGoogleBooks(titulo) { info ->
                            buscando = false
                            if (info == null) {
                                error = "No se encontraron resultados"
                            } else {
                                titulo = info.titulo
                                autor = info.autor
                                descripcion = info.descripcion
                                if (info.paginas > 0) {
                                    paginasText = info.paginas.toString()
                                }
                                if (info.imagenUrl.isNotBlank()) {
                                    imagenUri = info.imagenUrl
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !buscando
                ) {
                    Text(if (buscando) "Buscando..." else "Autocompletar con Google Books")
                }
            }
            item { Spacer(Modifier.height(Dimens.itemSpacing)) }
            item {
                OutlinedTextField(
                    value = autor,
                    onValueChange = { autor = it },
                    label = { Text("Autor") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = error.isNotEmpty()
                )
            }
            item { Spacer(Modifier.height(Dimens.itemSpacing)) }
            item {
                OutlinedTextField(
                    value = paginasText,
                    onValueChange = { paginasText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("Páginas") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = error.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    visualTransformation = VisualTransformation.None
                )
            }
            item { Spacer(Modifier.height(Dimens.itemSpacing)) }
            item {
                OutlinedTextField(
                    value = precioText,
                    onValueChange = { precioText = it.filter { ch -> ch.isDigit() || ch == '.' } },
                    label = { Text("Precio") },
                    modifier = Modifier.fillMaxWidth(),
                    isError = error.isNotEmpty(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    visualTransformation = VisualTransformation.None
                )
            }
            item { Spacer(Modifier.height(Dimens.itemSpacing)) }
            item {
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    isError = error.isNotEmpty()
                )
            }
            item { Spacer(Modifier.height(Dimens.itemSpacing)) }
            item {
                Button(onClick = {
                    pickerLauncher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                }, modifier = Modifier.fillMaxWidth()) {
                    Text(if (imagenUri.isBlank()) "Seleccionar imagen" else "Cambiar imagen")
                }
            }
            if (imagenUri.isNotBlank()) {
                item { Spacer(Modifier.height(Dimens.itemSpacing)) }
                item {
                    AsyncImage(
                        model = imagenUri,
                        contentDescription = "Vista previa",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                    )
                }
            }
            item {
                AnimatedVisibility(visible = error.isNotEmpty()) {
                    Column {
                        Spacer(Modifier.height(Dimens.itemSpacing / 2))
                        Text(error, color = MaterialTheme.colorScheme.error)
                    }
                }
            }
            item { Spacer(Modifier.height(Dimens.sectionSpacing)) }
            item {
                Button(
                    onClick = {
                        val paginas = paginasText.toIntOrNull() ?: 0
                        val precio = precioText.toDoubleOrNull() ?: 0.0
                        when {
                            titulo.isBlank() || autor.isBlank() || descripcion.isBlank() ->
                                error = "Completa título, autor y descripción"
                            paginas <= 0 -> error = "Páginas debe ser > 0"
                            precio <= 0.0 -> error = "Precio debe ser > 0"
                            else -> {
                                error = ""
                                appViewModel.agregarLibro(
                                    titulo = titulo,
                                    autor = autor,
                                    paginas = paginas,
                                    descripcion = descripcion,
                                    imagenUri = imagenUri,
                                    precio = precio
                                )
                                navController.navigate("home") {
                                    popUpTo("home") { inclusive = true }
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar libro")
                }
            }
        }
    }
}
