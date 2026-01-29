package com.example.evaluacion2.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.evaluacion2.data.entities.CarroEntity
import com.example.evaluacion2.data.entities.LibroEntity
import com.example.evaluacion2.data.entities.CarroConLibro

data class CarroConLibroResult(
    val id: Int,
    val titulo: String,
    val autor: String,
    val paginas: Int,
    val descripcion: String,
    val imagenUri: String,
    val precio: Double,
    val cantidad: Int
)

@Dao
interface CarroDao {
    
    @Insert
    suspend fun agregarAlCarro(carroItem: CarroEntity): Long
    
    @Delete
    suspend fun quitarDelCarro(carroItem: CarroEntity): Int
    
    @Query("SELECT * FROM carro WHERE usuarioEmail = :usuarioEmail")
    suspend fun obtenerCarroDelUsuario(usuarioEmail: String): List<CarroEntity>
    
    @Query("""
        SELECT l.id, l.titulo, l.autor, l.paginas, l.descripcion, l.imagenUri, l.precio, c.cantidad
        FROM libros l 
        INNER JOIN carro c ON l.id = c.libroId 
        WHERE c.usuarioEmail = :usuarioEmail 
        ORDER BY c.id DESC
    """)
    suspend fun obtenerCarroConCantidad(usuarioEmail: String): List<CarroConLibroResult>
    
    @Query("SELECT l.* FROM libros l INNER JOIN carro c ON l.id = c.libroId WHERE c.usuarioEmail = :usuarioEmail ORDER BY c.id DESC")
    suspend fun obtenerLibrosDelCarro(usuarioEmail: String): List<LibroEntity>
    
    @Query("DELETE FROM carro WHERE usuarioEmail = :usuarioEmail")
    suspend fun limpiarCarroDelUsuario(usuarioEmail: String): Int
    
    @Query("DELETE FROM carro WHERE usuarioEmail = :usuarioEmail AND libroId = :libroId")
    suspend fun quitarLibroDelCarro(usuarioEmail: String, libroId: Int): Int
    
    @Query("UPDATE carro SET cantidad = cantidad + 1 WHERE usuarioEmail = :usuarioEmail AND libroId = :libroId")
    suspend fun incrementarCantidad(usuarioEmail: String, libroId: Int): Int
    
    @Query("SELECT COUNT(*) FROM carro WHERE usuarioEmail = :usuarioEmail")
    suspend fun contarItemsEnCarro(usuarioEmail: String): Int
}
