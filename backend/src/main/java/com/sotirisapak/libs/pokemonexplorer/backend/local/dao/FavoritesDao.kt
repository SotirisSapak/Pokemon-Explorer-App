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

    /**
     *
     * __Nullable return value__
     *
     * Fetch all pokemon from local database as a mutable list to be manipulated.
     * @author SotirisSapak
     * @since 1.0.0
     */
    @Query("select * from Pokemon")
    suspend fun getAll(): MutableList<Pokemon>?

    /**
     *
     * __Nullable return value__
     *
     * Fetch a specific pokemon instance from local database based on given [id]
     * @author SotirisSapak
     * @since 1.0.0
     */
    @Query("select * from Pokemon where id = :id")
    suspend fun get(id: Int): Pokemon?

    /**
     * Insert new pokemon into favorites. You can add multiple pokemon separated with comma.
     * @author SotirisSapak
     * @since 1.0.0
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg pokemon: Pokemon)

    /**
     * Delete a specific [pokemon] from favorites.
     * @author SotirisSapak
     * @since 1.0.0
     */
    @Delete
    suspend fun delete(pokemon: Pokemon)
    
}