package com.sotirisapak.libs.pokemonexplorer.backend.remote.repositories

import com.sotirisapak.libs.pokemonexplorer.backend.models.PokemonType
import com.sotirisapak.libs.pokemonexplorer.backend.remote.endpoints.TypeEndpoints
import com.sotirisapak.libs.pokemonexplorer.core.models.ApiResult


/**
 * Repository template for pokemon types utilities
 * @author SotirisSapak
 * @since 1.0.0
 */
interface TypeRepository {

    /** The api to reference in order to build the retrofit instance */
    val api: TypeEndpoints

    /**
     * Get only a specific pokemon type based on given type id
     * @param id the type id to fetch its data
     * @return an [ApiResult] object to hold two result states (Success and failure). In Success state
     * ([ApiResult.Success]) the result will be a [PokemonType]. In Failure state, the error message
     * @author SotirisSapak
     * @since 1.0.0
     */
    suspend fun getTypeById(id: Int): ApiResult<PokemonType, String>

}