package com.example.colombiaturismo.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritoDao {

    /** Si el lugar ya estaba guardado por ese usuario no se duplica. */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertar(favorito: Favorito)

    @Query("DELETE FROM favoritos WHERE idUsuario = :idUsuario AND idLugar = :idLugar")
    suspend fun eliminar(idUsuario: String, idLugar: String): Int

    @Query("SELECT * FROM favoritos WHERE idUsuario = :idUsuario ORDER BY fechaGuardado DESC")
    fun observarPorUsuario(idUsuario: String): Flow<List<Favorito>>

    @Query("SELECT EXISTS(SELECT 1 FROM favoritos WHERE idUsuario = :idUsuario AND idLugar = :idLugar)")
    fun observarSiEstaGuardado(idUsuario: String, idLugar: String): Flow<Boolean>

    @Query("SELECT COUNT(*) FROM favoritos WHERE idUsuario = :idUsuario")
    fun observarCantidad(idUsuario: String): Flow<Int>
}
