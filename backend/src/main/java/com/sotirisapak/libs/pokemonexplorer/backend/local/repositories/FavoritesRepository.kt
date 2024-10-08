package com.sotirisapak.libs.pokemonexplorer.backend.local.repositories

import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon

/**
 * Repository template for pokemon favorites utilities
 * @author SotirisSapak
 * @since 1.0.0
 */
interface FavoritesRepository {

    /**
     * Get all favorites from local storage. This method will return an empty list instead of a null
     * value
     * @return the favorites list of pokemon or empty list
     * @author SotirisSapak
     * @since 1.0.0
     */
    suspend fun getFavorites(): List<Pokemon>

    /**
     * Private method to be used in [isFavorite] method. Will return a specific pokemon instance from
     * database based on given [pokemon] parameter.
     * @param pokemon the pokemon to fetch from favorites if exists
     * @return the pokemon instance itself or null if not exists.
     * @author SotirisSapak
     * @since 1.0.0
     */
    suspend fun getFavorite(pokemon: Pokemon): Pokemon?

    /**
     * Insert pokemon to favorites. You can add multiple pokemon separated by comma.
     * @param pokemon the list of pokemon to insert to favorites.
     * @author SotirisSapak
     * @since 1.0.0
     */
    suspend fun insertToFavorites(vararg pokemon: Pokemon)

    /**
     * Delete [pokemon] from favorites.
     * @param pokemon the pokemon to delete
     * @author SotirisSapak
     * @since 1.0.0
     */
    suspend fun deleteFromFavorites(pokemon: Pokemon)

    /**
     * Check if given [pokemon] exists in favorites.
     * @param pokemon the pokemon to check
     * @return true if exists, false if not!
     * @author SotirisSapak
     * @since 1.0.0
     */
    suspend fun isFavorite(pokemon: Pokemon): Boolean

}