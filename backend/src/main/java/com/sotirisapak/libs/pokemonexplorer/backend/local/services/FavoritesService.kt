package com.sotirisapak.libs.pokemonexplorer.backend.local.services

import com.sotirisapak.libs.pokemonexplorer.backend.local.FavoriteDatabase
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon

/**
 * The service to use in any viewModel to manipulate local database for favorite pokemon list.
 * @author SotirisSapak
 * @since 1.0.0
 */
class FavoritesService(favoriteDatabase: FavoriteDatabase) {

    /** The dao interface */
    private val dao = favoriteDatabase.favoritesDao()

    suspend fun getFavorites(): List<Pokemon> = dao.getAll() ?: emptyList()
    suspend fun getFavorite(pokemon: Pokemon): Pokemon? = dao.get(pokemon.id)
    suspend fun insertToFavorites(vararg pokemon: Pokemon) = dao.insert(*pokemon)
    suspend fun deleteFromFavorites(pokemon: Pokemon) = dao.delete(pokemon)
    suspend fun isFavorite(pokemon: Pokemon): Boolean {
        val response = getFavorite(pokemon)
        return response != null
    }
    
}