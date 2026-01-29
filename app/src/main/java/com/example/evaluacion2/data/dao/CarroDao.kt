package com.example.evaluacion2.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.evaluacion2.data.entities.CarroEntity
import com.example.evaluacion2.data.entities.LibroEntity

@Dao
interface CarroDao {
    
    @Insert
    suspend fun agregarAlCarro(carroItem: CarroEntity): Long
    
    @Delete
    suspend fun quitarDelCarro(carroItem: CarroEntity): Int
    
    @Query("SELECT * FROM carro WHERE usuarioEmail = :usuarioEmail")
    suspend fun obtenerCarroDelUsuario(usuarioEmail: String): List<CarroEntity>
    
    @Query("SELECT l.* FROM libros l INNER JOIN carro c ON l.id = c.libroId WHERE c.usuarioEmail = :usuarioEmail")
    suspend fun obtenerLibrosDelCarro(usuarioEmail: String): List<LibroEntity>
    
    @Query("DELETE FROM carro WHERE usuarioEmail = :usuarioEmail")
    suspend fun limpiarCarroDelUsuario(usuarioEmail: String): Int
    
    @Query("DELETE FROM carro WHERE usuarioEmail = :usuarioEmail AND libroId = :libroId")
    suspend fun quitarLibroDelCarro(usuarioEmail: String, libroId: Int): Int
    
    @Query("SELECT COUNT(*) FROM carro WHERE usuarioEmail = :usuarioEmail")
    suspend fun contarItemsEnCarro(usuarioEmail: String): Int
}
