package com.sotirisapak.libs.pokemonexplorer.backend.remote.repositories

import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon
import com.sotirisapak.libs.pokemonexplorer.backend.models.PokemonType
import com.sotirisapak.libs.pokemonexplorer.core.models.ApiResult

/**
 * Repository template for pokemon utilities
 * @author SotirisSapak
 * @since 1.0.0
 */
interface PokemonRepository {

    /**
     * Fetch [Pokemon] object based on given [endpointUrl]. This url is referenced from
     * [PokemonType.TypePokemon.PokemonInformation.url] object.
     * @param endpointUrl the specific url to fetch the pokemon
     * @return an [ApiResult] object to hold two result states (Success and failure). In Success state
     * ([ApiResult.Success]) the result will be a new [Pokemon] instance. In Failure state,
     * the error message
     * @author SotirisSapak
     * @since 1.0.0
     */
    suspend fun getPokemonByUrl(endpointUrl: String): ApiResult<Pokemon, String>
    
}