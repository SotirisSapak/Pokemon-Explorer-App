package com.sotirisapak.libs.pokemonexplorer.backend.local.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sotirisapak.libs.pokemonexplorer.backend.local.converters.StatsConverter
import com.sotirisapak.libs.pokemonexplorer.backend.local.dao.FavoritesDao
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon

/**
 * The database instance to use across the entire application for local favorites database
 * @author SotirisSapak
 * @since 1.0.0
 */
@Database(
    entities = [Pokemon::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StatsConverter::class)
abstract class FavoriteDatabase: RoomDatabase() {

    /**
     * The [FavoritesDao] interface to use in order to manipulate Favorites in local database
     * @author SotirisSapak
     * @since 1.0.0
     */
    abstract fun favoritesDao(): FavoritesDao

}