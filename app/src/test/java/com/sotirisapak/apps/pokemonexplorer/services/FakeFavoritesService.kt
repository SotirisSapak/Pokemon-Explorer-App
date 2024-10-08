package com.sotirisapak.apps.pokemonexplorer.services

import com.sotirisapak.libs.pokemonexplorer.backend.local.repositories.FavoritesRepository
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon

/**
 * Fake favorites service to provide unit tests for viewModels
 * @author SotirisSapak
 * @since 1.0.0
 */
class FakeFavoritesService: FavoritesRepository {

    private val listOfPokemon = mutableListOf<Pokemon>()

    /**
     * Get all favorites from local storage. This method will return an empty list instead of a null
     * value
     * @return the favorites list of pokemon or empty list
     * @author SotirisSapak
     * @since 1.0.0
     */
    override suspend fun getFavorites() = listOfPokemon.toList()

    /**
     * Private method to be used in [isFavorite] method. Will return a specific pokemon instance from
     * database based on given [pokemon] parameter.
     * @param pokemon the pokemon to fetch from favorites if exists
     * @return the pokemon instance itself or null if not exists.
     * @author SotirisSapak
     * @since 1.0.0
     */
    override suspend fun getFavorite(pokemon: Pokemon): Pokemon? {
        return listOfPokemon.find { it == pokemon }
    }

    /**
     * Insert pokemon to favorites. You can add multiple pokemon separated by comma.
     * @param pokemon the list of pokemon to insert to favorites.
     * @author SotirisSapak
     * @since 1.0.0
     */
    override suspend fun insertToFavorites(vararg pokemon: Pokemon) {
        listOfPokemon.addAll(pokemon)
    }

    /**
     * Delete [pokemon] from favorites.
     * @param pokemon the pokemon to delete
     * @author SotirisSapak
     * @since 1.0.0
     */
    override suspend fun deleteFromFavorites(pokemon: Pokemon) {
        listOfPokemon.remove(pokemon)
    }

    /**
     * Check if given [pokemon] exists in favorites.
     * @param pokemon the pokemon to check
     * @return true if exists, false if not!
     * @author SotirisSapak
     * @since 1.0.0
     */
    override suspend fun isFavorite(pokemon: Pokemon): Boolean {
        val pokemonFound = listOfPokemon.find { it == pokemon }
        return pokemonFound != null
    }

}