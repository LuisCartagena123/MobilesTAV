package com.example.evaluacion2.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "libros")
data class LibroEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val autor: String,
    val paginas: Int,
    val descripcion: String,
    val imagenUri: String = "",
    val precio: Double = 0.0
)
