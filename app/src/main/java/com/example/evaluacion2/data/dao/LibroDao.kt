package com.example.evaluacion2.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.evaluacion2.data.entities.LibroEntity

@Dao
interface LibroDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBook(book: LibroEntity): Long

    @Query("SELECT * FROM libros ORDER BY id")
    suspend fun getBooksOrdered(): List<LibroEntity>

    @Query("DELETE FROM libros WHERE id = :id")
    suspend fun deleteById(id: Int): Int
}
