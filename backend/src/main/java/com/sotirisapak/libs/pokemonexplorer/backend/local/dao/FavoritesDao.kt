package com.sotirisapak.libs.pokemonexplorer.backend.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon

/**
 * Interface to generate requests to local room database to manipulate favorites data
 * @author SotirisSapak
 * @since 1.0.0
 */
@Dao
interface FavoritesDao {

    @Query("select * from Pokemon")
    suspend fun getAll(): MutableList<Pokemon>?

    @Query("select * from Pokemon where id = :id")
    suspend fun get(id: Int): Pokemon?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg pokemon: Pokemon)

    @Delete
    suspend fun delete(pokemon: Pokemon)
    
}