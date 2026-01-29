package com.example.evaluacion2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.ViewModelProvider
import com.example.evaluacion2.data.RoomRepository
import com.example.evaluacion2.data.entities.UsuarioEntity
import com.example.evaluacion2.data.entities.LibroEntity
import com.example.evaluacion2.data.entities.CarroConLibro
import com.example.evaluacion2.data.network.GoogleBookInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppViewModel(private val repository: RoomRepository) : ViewModel() {

    private val _usuarioActual = MutableStateFlow<UsuarioEntity?>(null)
    val usuarioActual: StateFlow<UsuarioEntity?> = _usuarioActual

    private val _libros = MutableStateFlow<List<LibroEntity>>(emptyList())
    val libros: StateFlow<List<LibroEntity>> = _libros

    private val _carro = MutableStateFlow<List<CarroConLibro>>(emptyList())
    val carro: StateFlow<List<CarroConLibro>> = _carro

    private val _usuarios = MutableStateFlow<List<UsuarioEntity>>(emptyList())
    val usuarios: StateFlow<List<UsuarioEntity>> = _usuarios

    fun registrarUsuario(email: String, username: String, nombre: String, password: String, isAdmin: Boolean = false, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val ok = repository.registrarUsuario(email, username, nombre, password, isAdmin)
            if (ok) {
                _usuarioActual.value = UsuarioEntity(email = email, username = username, nombre = nombre, password = password, isAdmin = isAdmin)
                _libros.value = emptyList()
                _carro.value = emptyList()
            }
            onResult(ok)
        }
    }

    fun login(identifier: String, password: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val user = repository.login(identifier, password)
            if (user != null) {
                _usuarioActual.value = user
                _libros.value = repository.obtenerLibros()
                _carro.value = repository.obtenerCarroDelUsuario(user.email)
                onResult(true)
            } else {
                onResult(false)
            }
        }
    }

    fun logout() {
        _usuarioActual.value = null
        _libros.value = emptyList()
        _carro.value = emptyList()
    }

    fun agregarLibro(titulo: String, autor: String, paginas: Int, descripcion: String, imagenUri: String, precio: Double) {
        viewModelScope.launch {
            repository.agregarLibro(titulo, autor, paginas, descripcion, imagenUri, precio)
            _libros.value = repository.obtenerLibros()
        }
    }

    fun agregarAlCarro(libro: LibroEntity) {
        val usuarioActualValue = _usuarioActual.value ?: return
        viewModelScope.launch {
            repository.agregarAlCarro(usuarioActualValue.email, libro.id)
            _carro.value = repository.obtenerCarroDelUsuario(usuarioActualValue.email)
        }
    }

    fun quitarDelCarro(libro: LibroEntity) {
        val usuarioActualValue = _usuarioActual.value ?: return
        viewModelScope.launch {
            repository.quitarDelCarro(usuarioActualValue.email, libro.id)
            _carro.value = repository.obtenerCarroDelUsuario(usuarioActualValue.email)
        }
    }

    fun borrarLibro(libro: LibroEntity) {
        viewModelScope.launch {
            repository.borrarLibro(libro.id)
            _libros.value = repository.obtenerLibros()
        }
    }

    fun cargarUsuarios() {
        viewModelScope.launch {
            _usuarios.value = repository.obtenerTodosUsuarios()
        }
    }

    fun cambiarIsAdmin(email: String, isAdmin: Boolean) {
        viewModelScope.launch {
            repository.actualizarIsAdmin(email, isAdmin)
            _usuarios.value = repository.obtenerTodosUsuarios()
        }
    }

    fun buscarLibroGoogleBooks(query: String, onResult: (GoogleBookInfo?) -> Unit) {
        viewModelScope.launch {
            val info = repository.buscarLibroGoogleBooks(query)
            onResult(info)
        }
    }

    companion object {
        fun provideFactory(repo: RoomRepository): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return AppViewModel(repo) as T
                }
            }
    }
}
