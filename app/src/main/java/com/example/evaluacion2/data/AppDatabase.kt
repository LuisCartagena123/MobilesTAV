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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [UsuarioEntity::class, LibroEntity::class, CarroEntity::class], version = 8, exportSchema = false)
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
                    CoroutineScope(Dispatchers.IO).launch {
                        insertAdminIfNeeded(it.userDao())
                    }
                }
            }

        private suspend fun insertAdminIfNeeded(userDao: UserDao) {
            val adminEmail = "ad@a.cl"
            if (userDao.countByEmail(adminEmail) == 0) {
                val adminUser = UsuarioEntity(
                    email = adminEmail,
                    username = "ad",
                    nombre = "Administrador",
                    password = "ad123",
                    isAdmin = true
                )
                userDao.insertUser(adminUser)
            }
        }
    }
}

