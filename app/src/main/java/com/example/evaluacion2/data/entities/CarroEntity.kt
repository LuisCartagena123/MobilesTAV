package com.example.evaluacion2.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "carro",
    foreignKeys = [
        ForeignKey(
            entity = UsuarioEntity::class,
            parentColumns = ["email"],
            childColumns = ["usuarioEmail"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = LibroEntity::class,
            parentColumns = ["id"],
            childColumns = ["libroId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("usuarioEmail"),
        Index("libroId"),
        Index(value = ["usuarioEmail", "libroId"], unique = true)
    ]
)
data class CarroEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val usuarioEmail: String,
    val libroId: Int
)
