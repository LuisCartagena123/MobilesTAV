package com.example.evaluacion2.data.network

import retrofit2.Response
import retrofit2.http.*

// DTOs para comunicación con backend
data class AuthRequest(
    val identifier: String,
    val password: String
)

data class RegistroRequest(
    val email: String,
    val username: String,
    val nombre: String,
    val password: String
)

data class AuthResponse(
    val token: String,
    val type: String? = null,
    val email: String,
    val username: String,
    val nombre: String,
    val isAdmin: Boolean
)

data class UsuarioDTO(
    val email: String,
    val username: String? = null,
    val nombre: String? = null,
    val isAdmin: Boolean? = null
)

data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null,
    val error: String? = null
)

data class LibroResponse(
    val id: Int? = null,
    val titulo: String,
    val autor: String,
    val paginas: Int,
    val descripcion: String,
    val imagenUri: String,
    val precio: Double
)

data class CarroItemResponse(
    val id: Int,
    val libro: LibroResponse,
    val usuarioEmail: String,
    val cantidad: Int,
    val precioUnitario: Double
)

data class AgregarCarroRequest(
    val libroId: Int,
    val cantidad: Int
)

interface BackendApi {
    // Auth endpoints
    @POST("auth/registro")
    suspend fun registro(@Body request: RegistroRequest): Response<ApiResponse<AuthResponse>>

    @POST("auth/login")
    suspend fun login(@Body request: AuthRequest): Response<ApiResponse<AuthResponse>>

    // Libros endpoints
    @GET("libros")
    suspend fun obtenerLibros(): Response<ApiResponse<List<LibroResponse>>>

    @GET("libros/{id}")
    suspend fun obtenerLibro(@Path("id") id: Int): Response<ApiResponse<LibroResponse>>

    @POST("libros")
    suspend fun crearLibro(
        @Header("Authorization") token: String,
        @Body libro: LibroResponse
    ): Response<ApiResponse<LibroResponse>>

    @PUT("libros/{id}")
    suspend fun actualizarLibro(
        @Path("id") id: Int,
        @Header("Authorization") token: String,
        @Body libro: LibroResponse
    ): Response<ApiResponse<LibroResponse>>

    @DELETE("libros/{id}")
    suspend fun eliminarLibro(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Void>>

    // Usuarios endpoints
    @GET("usuarios")
    suspend fun obtenerUsuarios(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<UsuarioDTO>>>

    @GET("usuarios/{email}")
    suspend fun obtenerUsuario(
        @Path("email") email: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<UsuarioDTO>>

    @PUT("usuarios/{email}/admin")
    suspend fun hacerAdmin(
        @Path("email") email: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<UsuarioDTO>>

    @PUT("usuarios/{email}/no-admin")
    suspend fun quitarAdmin(
        @Path("email") email: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<UsuarioDTO>>

    @DELETE("usuarios/{email}")
    suspend fun eliminarUsuario(
        @Path("email") email: String,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Void>>

    // Carro endpoints (cambiado a /carros para coincidir con backend)
    @GET("carros")
    suspend fun obtenerCarro(
        @Header("Authorization") token: String
    ): Response<ApiResponse<List<CarroItemResponse>>>

    @POST("carros")
    suspend fun agregarAlCarro(
        @Header("Authorization") token: String,
        @Body data: AgregarCarroRequest
    ): Response<ApiResponse<CarroItemResponse>>

    @PUT("carros/{id}")
    suspend fun actualizarCantidad(
        @Path("id") id: Int,
        @Query("cantidad") cantidad: Int,
        @Header("Authorization") token: String
    ): Response<ApiResponse<CarroItemResponse>>

    @DELETE("carros/{id}")
    suspend fun quitarDelCarro(
        @Path("id") id: Int,
        @Header("Authorization") token: String
    ): Response<ApiResponse<Void>>

    @GET("carros/total")
    suspend fun obtenerTotal(
        @Header("Authorization") token: String
    ): Response<ApiResponse<Map<String, Double>>>
}
