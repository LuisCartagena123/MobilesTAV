package com.example.evaluacion2.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.evaluacion2.data.entities.UsuarioEntity
import com.example.evaluacion2.data.entities.LibroEntity
import com.example.evaluacion2.data.entities.CarroEntity
import com.example.evaluacion2.data.entities.CarroConLibro
import com.example.evaluacion2.data.network.GoogleBookInfo
import com.example.evaluacion2.data.network.GoogleBooksClient
import com.example.evaluacion2.data.network.BackendClient
import com.example.evaluacion2.data.network.RegistroRequest
import com.example.evaluacion2.data.network.AuthRequest
import com.example.evaluacion2.data.network.LibroResponse
import com.example.evaluacion2.data.network.UsuarioDTO
import com.example.evaluacion2.data.network.AgregarCarroRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomRepository(context: Context) {
    private val db = AppDatabase.getInstance(context)
    private val userDao = db.userDao()
    private val libroDao = db.libroDao()
    private val carroDao = db.carroDao()
    private val googleBooksApi = GoogleBooksClient.api
    private val backendApi = BackendClient.api
    private val sharedPreferences: SharedPreferences = try {
        val masterKey = MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build()
        EncryptedSharedPreferences.create(
            context,
            "backend_prefs",
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (e: Exception) {
        context.getSharedPreferences("backend_prefs", Context.MODE_PRIVATE)
    }

    fun guardarToken(token: String) {
        sharedPreferences.edit().putString("jwt_token", token).apply()
    }

    fun obtenerToken(): String? {
        return sharedPreferences.getString("jwt_token", null)
    }

    fun limpiarToken() {
        sharedPreferences.edit().remove("jwt_token").apply()
    }

    suspend fun registrarUsuario(email: String, username: String, nombre: String, password: String, isAdmin: Boolean = false): Boolean = withContext(Dispatchers.IO) {
        try {
            val response = backendApi.registro(
                RegistroRequest(email, username, nombre, password)
            )
            if (response.isSuccessful && response.body()?.success == true) {
                val authResponse = response.body()?.data
                authResponse?.token?.let { guardarToken(it) }
                userDao.insertUser(UsuarioEntity(email = email, username = username, nombre = nombre, password = password, isAdmin = isAdmin))
                return@withContext true
            }
            return@withContext false
        } catch (e: Exception) {
            if (userDao.countByEmail(email) > 0) return@withContext false
            if (userDao.countByUsername(username) > 0) return@withContext false
            userDao.insertUser(UsuarioEntity(email = email, username = username, nombre = nombre, password = password, isAdmin = isAdmin)) != -1L
        }
    }

    suspend fun login(identifier: String, password: String): UsuarioEntity? = withContext(Dispatchers.IO) {
        try {
            val response = backendApi.login(AuthRequest(identifier, password))
            if (response.isSuccessful && response.body()?.success == true) {
                val authResponse = response.body()?.data
                authResponse?.token?.let { guardarToken(it) }
                authResponse?.let { usuario ->
                    val entity = UsuarioEntity(
                        email = usuario.email,
                        username = usuario.username,
                        nombre = usuario.nombre,
                        isAdmin = usuario.isAdmin,
                        password = password
                    )
                    userDao.insertUser(entity)
                    return@withContext entity
                }
            }
            return@withContext null
        } catch (e: Exception) {
            return@withContext userDao.getByIdentifierAndPassword(identifier, password)
        }
    }

    suspend fun agregarLibro(titulo: String, autor: String, paginas: Int, descripcion: String, imagenUri: String, precio: Double) = withContext(Dispatchers.IO) {
        try {
            val token = obtenerToken() ?: return@withContext
            val response = backendApi.crearLibro("Bearer $token", LibroResponse(null, titulo, autor, paginas, descripcion, imagenUri, precio))
            if (response.isSuccessful && response.body()?.success == true) {
                response.body()?.data?.let { dto ->
                    libroDao.insertBook(
                        LibroEntity(
                            id = (dto.id ?: 0L).toInt(),
                            titulo = dto.titulo,
                            autor = dto.autor,
                            paginas = dto.paginas,
                            descripcion = dto.descripcion,
                            imagenUri = dto.imagenUri,
                            precio = dto.precio
                        )
                    )
                }
            }
        } catch (e: Exception) {
            libroDao.insertBook(LibroEntity(titulo = titulo, autor = autor, paginas = paginas, descripcion = descripcion, imagenUri = imagenUri, precio = precio))
        }
    }

    suspend fun obtenerLibros(): List<LibroEntity> = withContext(Dispatchers.IO) {
        try {
            val response = backendApi.obtenerLibros()
            if (response.isSuccessful && response.body()?.success == true) {
                val librosDto = response.body()?.data ?: emptyList()
                val librosEntity = librosDto.map { dto ->
                    LibroEntity(id = (dto.id ?: 0L).toInt(), titulo = dto.titulo, autor = dto.autor, paginas = dto.paginas, descripcion = dto.descripcion, imagenUri = dto.imagenUri, precio = dto.precio)
                }
                // Actualizamos la base de datos local con los datos del servidor
                librosEntity.forEach { libro ->
                    try {
                        libroDao.insertBook(libro)
                    } catch (e: Exception) {
                        // Ignorar si hay conflicto
                    }
                }
                return@withContext librosEntity
            }
            return@withContext libroDao.getBooksOrdered()
        } catch (e: Exception) {
            return@withContext libroDao.getBooksOrdered()
        }
    }

    suspend fun borrarLibro(libroId: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            val token = obtenerToken() ?: return@withContext false
            val response = backendApi.eliminarLibro(libroId, "Bearer $token")
            if (response.isSuccessful) {
                libroDao.deleteById(libroId)
                return@withContext true
            }
            return@withContext false
        } catch (e: Exception) {
            return@withContext libroDao.deleteById(libroId) > 0
        }
    }

    suspend fun hasAnyUser(): Boolean = withContext(Dispatchers.IO) { userDao.countAll() > 0 }

    suspend fun obtenerTodosUsuarios(): List<UsuarioEntity> = withContext(Dispatchers.IO) {
        try {
            val token = obtenerToken() ?: return@withContext userDao.getAllUsers()
            val response = backendApi.obtenerUsuarios("Bearer $token")
            if (response.isSuccessful && response.body()?.success == true) {
                val usuarios = response.body()?.data ?: emptyList()
                return@withContext usuarios.map { dto ->
                    UsuarioEntity(email = dto.email, username = dto.username ?: "", nombre = dto.nombre ?: "", isAdmin = dto.isAdmin ?: false, password = "")
                }
            }
            return@withContext userDao.getAllUsers()
        } catch (e: Exception) {
            return@withContext userDao.getAllUsers()
        }
    }

    suspend fun actualizarIsAdmin(email: String, isAdmin: Boolean) = withContext(Dispatchers.IO) {
        try {
            val token = obtenerToken() ?: return@withContext
            val response = if (isAdmin) {
                backendApi.hacerAdmin(email, "Bearer $token")
            } else {
                backendApi.quitarAdmin(email, "Bearer $token")
            }
            if (response.isSuccessful) {
                userDao.updateIsAdmin(email, isAdmin)
            }
        } catch (e: Exception) {
            userDao.updateIsAdmin(email, isAdmin)
        }
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

    suspend fun agregarAlCarro(usuarioEmail: String, libroId: Int, cantidad: Int = 1) = withContext(Dispatchers.IO) {
        try {
            val token = obtenerToken() ?: return@withContext
            val response = backendApi.agregarAlCarro("Bearer $token", AgregarCarroRequest(libroId, cantidad))
            if (response.isSuccessful && response.body()?.success == true) {
                // Sincronizar el libro en Room si no existe
                val libroExiste = try {
                    libroDao.getBooksOrdered().any { it.id == libroId }
                } catch (e: Exception) {
                    false
                }
                
                if (!libroExiste) {
                    // Obtener el libro del backend y guardarlo en Room
                    val libroResponse = backendApi.obtenerLibro(libroId)
                    if (libroResponse.isSuccessful && libroResponse.body()?.success == true) {
                        libroResponse.body()?.data?.let { dto ->
                            val libroEntity = LibroEntity(
                                id = (dto.id ?: 0L).toInt(),
                                titulo = dto.titulo,
                                autor = dto.autor,
                                paginas = dto.paginas,
                                descripcion = dto.descripcion,
                                imagenUri = dto.imagenUri,
                                precio = dto.precio
                            )
                            libroDao.insertBook(libroEntity)
                        }
                    }
                }
                
                // Agregar o incrementar en el carrito local
                val existente = carroDao.obtenerCarroDelUsuario(usuarioEmail).find { it.libroId == libroId }
                if (existente != null) {
                    carroDao.incrementarCantidad(usuarioEmail, libroId)
                } else {
                    carroDao.agregarAlCarro(CarroEntity(usuarioEmail = usuarioEmail, libroId = libroId, cantidad = cantidad))
                }
            }
        } catch (e: Exception) {
            // Modo offline: solo agregar a Room si el libro ya existe
            val existente = carroDao.obtenerCarroDelUsuario(usuarioEmail).find { it.libroId == libroId }
            if (existente != null) {
                carroDao.incrementarCantidad(usuarioEmail, libroId)
            } else {
                try {
                    carroDao.agregarAlCarro(CarroEntity(usuarioEmail = usuarioEmail, libroId = libroId, cantidad = cantidad))
                } catch (fkException: Exception) {
                    // No se puede agregar porque el libro no existe en Room
                }
            }
        }
    }

    suspend fun quitarDelCarro(usuarioEmail: String, libroId: Int): Boolean = withContext(Dispatchers.IO) {
        try {
            val token = obtenerToken() ?: return@withContext false
            val responseCarro = backendApi.obtenerCarro("Bearer $token")
            if (responseCarro.isSuccessful && responseCarro.body()?.success == true) {
                val item = responseCarro.body()?.data?.firstOrNull { it.libro?.id?.toInt() == libroId }
                if (item != null) {
                    val response = backendApi.quitarDelCarro(item.id, "Bearer $token")
                    if (response.isSuccessful) {
                        carroDao.quitarLibroDelCarro(usuarioEmail, libroId)
                        return@withContext true
                    }
                }
            }
            return@withContext false
        } catch (e: Exception) {
            return@withContext carroDao.quitarLibroDelCarro(usuarioEmail, libroId) > 0
        }
    }

    suspend fun obtenerCarroDelUsuario(usuarioEmail: String): List<CarroConLibro> = withContext(Dispatchers.IO) {
        try {
            val token = obtenerToken() ?: return@withContext obtenerCarroLocal(usuarioEmail)
            val response = backendApi.obtenerCarro("Bearer $token")
            if (response.isSuccessful && response.body()?.success == true) {
                val items = response.body()?.data ?: emptyList()
                return@withContext items.mapNotNull { item ->
                    item.libro?.let { dto ->
                        CarroConLibro(
                            libro = LibroEntity(
                                id = (dto.id ?: 0L).toInt(),
                                titulo = dto.titulo,
                                autor = dto.autor,
                                paginas = dto.paginas,
                                descripcion = dto.descripcion,
                                imagenUri = dto.imagenUri,
                                precio = dto.precio
                            ),
                            cantidad = item.cantidad
                        )
                    }
                }
            }
            return@withContext obtenerCarroLocal(usuarioEmail)
        } catch (e: Exception) {
            return@withContext obtenerCarroLocal(usuarioEmail)
        }
    }

    private suspend fun obtenerCarroLocal(usuarioEmail: String): List<CarroConLibro> {
        val resultados = carroDao.obtenerCarroConCantidad(usuarioEmail)
        return resultados.map { result ->
            CarroConLibro(
                libro = LibroEntity(
                    id = result.id,
                    titulo = result.titulo,
                    autor = result.autor,
                    paginas = result.paginas,
                    descripcion = result.descripcion,
                    imagenUri = result.imagenUri,
                    precio = result.precio
                ),
                cantidad = result.cantidad
            )
        }
    }

    suspend fun limpiarCarroDelUsuario(usuarioEmail: String) = withContext(Dispatchers.IO) {
        carroDao.limpiarCarroDelUsuario(usuarioEmail)
    }
}

