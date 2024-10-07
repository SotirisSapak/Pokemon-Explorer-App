package com.sotirisapak.libs.pokemonexplorer.backend.remote.services

import com.sotirisapak.libs.pokemonexplorer.backend.remote.PokemonApi
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon
import com.sotirisapak.libs.pokemonexplorer.backend.models.PokemonType
import com.sotirisapak.libs.pokemonexplorer.backend.remote.endpoints.PokemonEndpoints
import com.sotirisapak.libs.pokemonexplorer.backend.remote.repositories.PokemonRepository
import com.sotirisapak.libs.pokemonexplorer.core.models.ApiResult
import retrofit2.Retrofit

/**
 * Api service to implement [PokemonEndpoints] methods
 * @param retrofit pass the retrofit instance ([PokemonApi.instance])
 * @author SotirisSapak
 * @since 1.0.0
 */
class PokemonService(retrofit: Retrofit): PokemonRepository {

    /** The api to reference in order to build the retrofit instance */
    override val api = retrofit.create(PokemonEndpoints::class.java)

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
    override suspend fun getPokemonByUrl(endpointUrl: String): ApiResult<Pokemon, String> {
        try {
            // we should properly execute the retrofit
            val result = api.getPokemon(endpointUrl)
            // first, check the result if is successful or not
            if (result.isSuccessful && result.body() != null) {
                // return directly the result data
                return ApiResult.onSuccess(result.body() ?: Pokemon())
            }
            // define which state the result is...if response code is not [200, 300) or if body is null
            if(!result.isSuccessful) {
                return ApiResult.onFailure("Response code: ${result.code()} (${result.message()})")
            }
            // result is successful but return a null body
            return ApiResult.onFailure("Response code: ${result.code()} (The request body is null)")
        } catch (ex: Exception) {
            return ApiResult.onFailure(ex.message ?: "Unknown error occurred")
        }
    }

}