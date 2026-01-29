package com.example.evaluacion2.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

import androidx.room.Index

@Entity(
    tableName = "usuarios",
    indices = [Index(value = ["username"], unique = true)]
)
data class UsuarioEntity(
    @PrimaryKey val email: String,
    val username: String,
    val nombre: String,
    val password: String,
    val isAdmin: Boolean = false
)
