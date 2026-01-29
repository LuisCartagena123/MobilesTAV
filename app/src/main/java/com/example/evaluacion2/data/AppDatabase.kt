package com.example.evaluacion2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.evaluacion2.data.dao.LibroDao
import com.example.evaluacion2.data.dao.UserDao
import com.example.evaluacion2.data.dao.CarroDao
import com.example.evaluacion2.data.entities.LibroEntity
import com.example.evaluacion2.data.entities.UsuarioEntity
import com.example.evaluacion2.data.entities.CarroEntity

@Database(entities = [UsuarioEntity::class, LibroEntity::class, CarroEntity::class], version = 9, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun libroDao(): LibroDao
    abstract fun carroDao(): CarroDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_libros.db"
                ).fallbackToDestructiveMigration()
                .build().also {
                    INSTANCE = it
                }
            }
    }
}

