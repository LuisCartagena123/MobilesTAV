package com.example.evaluacion2.data

import android.content.Context
import com.example.evaluacion2.data.entities.UsuarioEntity
import com.example.evaluacion2.data.entities.LibroEntity
import com.example.evaluacion2.data.entities.CarroEntity
import com.example.evaluacion2.data.network.GoogleBookInfo
import com.example.evaluacion2.data.network.GoogleBooksClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val userDao = db.userDao()
    private val libroDao = db.libroDao()
    private val carroDao = db.carroDao()
    private val googleBooksApi = GoogleBooksClient.api

    suspend fun registrarUsuario(email: String, username: String, nombre: String, password: String, isAdmin: Boolean = false): Boolean = withContext(Dispatchers.IO) {
        if (userDao.countByEmail(email) > 0) return@withContext false
        if (userDao.countByUsername(username) > 0) return@withContext false
        userDao.insertUser(UsuarioEntity(email = email, username = username, nombre = nombre, password = password, isAdmin = isAdmin)) != -1L
    }

    suspend fun login(identifier: String, password: String): UsuarioEntity? = withContext(Dispatchers.IO) {
        userDao.getByIdentifierAndPassword(identifier, password)
    }

    suspend fun agregarLibro(titulo: String, autor: String, paginas: Int, descripcion: String, imagenUri: String, precio: Double) = withContext(Dispatchers.IO) {
        libroDao.insertBook(
            LibroEntity(
                titulo = titulo,
                autor = autor,
                paginas = paginas,
                descripcion = descripcion,
                imagenUri = imagenUri,
                precio = precio
            )
        )
    }

    suspend fun obtenerLibros(): List<LibroEntity> = withContext(Dispatchers.IO) {
        libroDao.getBooksOrdered()
    }

    suspend fun borrarLibro(position: Int): Boolean = withContext(Dispatchers.IO) {
        val list = libroDao.getBooksOrdered()
        if (position in list.indices) {
            val id = list[position].id
            libroDao.deleteById(id) > 0
        } else false
    }

    suspend fun hasAnyUser(): Boolean = withContext(Dispatchers.IO) { userDao.countAll() > 0 }

    suspend fun obtenerTodosUsuarios(): List<UsuarioEntity> = withContext(Dispatchers.IO) {
        userDao.getAllUsers()
    }

    suspend fun actualizarIsAdmin(email: String, isAdmin: Boolean) = withContext(Dispatchers.IO) {
        userDao.updateIsAdmin(email, isAdmin)
    }

    suspend fun buscarLibroGoogleBooks(query: String): GoogleBookInfo? = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext null
        val response = googleBooksApi.searchBooks(query.trim(), 1)
        val item = response.items?.firstOrNull() ?: return@withContext null
        val info = item.volumeInfo

        GoogleBookInfo(
            titulo = info.title ?: query,
            autor = info.authors?.joinToString(", ") ?: "",
            descripcion = info.description ?: "",
            paginas = info.pageCount ?: 0,
            imagenUrl = info.imageLinks?.thumbnail?.replace("http://", "https://") ?: ""
        )
    }

    // Operaciones del carro
    suspend fun agregarAlCarro(usuarioEmail: String, libroId: Int) = withContext(Dispatchers.IO) {
        carroDao.agregarAlCarro(CarroEntity(usuarioEmail = usuarioEmail, libroId = libroId)) != -1L
    }

    suspend fun quitarDelCarro(usuarioEmail: String, libroId: Int): Boolean = withContext(Dispatchers.IO) {
        carroDao.quitarLibroDelCarro(usuarioEmail, libroId) > 0
    }

    suspend fun obtenerCarroDelUsuario(usuarioEmail: String): List<LibroEntity> = withContext(Dispatchers.IO) {
        carroDao.obtenerLibrosDelCarro(usuarioEmail)
    }

    suspend fun limpiarCarroDelUsuario(usuarioEmail: String) = withContext(Dispatchers.IO) {
        carroDao.limpiarCarroDelUsuario(usuarioEmail)
    }
}
