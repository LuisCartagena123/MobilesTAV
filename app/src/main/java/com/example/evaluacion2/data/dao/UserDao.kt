package com.example.evaluacion2.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.evaluacion2.data.entities.UsuarioEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UsuarioEntity): Long

    @Query("SELECT COUNT(*) FROM usuarios WHERE email = :email")
    suspend fun countByEmail(email: String): Int

    @Query("SELECT COUNT(*) FROM usuarios WHERE username = :username")
    suspend fun countByUsername(username: String): Int

    @Query("SELECT COUNT(*) FROM usuarios")
    suspend fun countAll(): Int

    @Query("SELECT * FROM usuarios WHERE (email = :identifier OR username = :identifier) AND password = :password LIMIT 1")
    suspend fun getByIdentifierAndPassword(identifier: String, password: String): UsuarioEntity?

    @Query("SELECT * FROM usuarios ORDER BY email")
    suspend fun getAllUsers(): List<UsuarioEntity>

    @Query("UPDATE usuarios SET isAdmin = :isAdmin WHERE email = :email")
    suspend fun updateIsAdmin(email: String, isAdmin: Boolean)
}
