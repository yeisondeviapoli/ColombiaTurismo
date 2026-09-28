package com.example.colombiaturismo.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Usuario::class, Favorito::class],
    version = 1,
    exportSchema = false
)
abstract class ColomViaDatabase : RoomDatabase() {

    abstract fun usuarioDao(): UsuarioDao

    abstract fun favoritoDao(): FavoritoDao

    companion object {
        fun crear(context: Context): ColomViaDatabase =
            Room.databaseBuilder(context.applicationContext, ColomViaDatabase::class.java, "colomvia.db")
                .build()
    }
}
