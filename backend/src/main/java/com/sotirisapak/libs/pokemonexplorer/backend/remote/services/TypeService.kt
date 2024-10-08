package com.sotirisapak.libs.pokemonexplorer.backend.remote.services

import android.util.Log
import com.sotirisapak.libs.pokemonexplorer.backend.remote.PokemonApi
import com.sotirisapak.libs.pokemonexplorer.backend.models.PokemonType
import com.sotirisapak.libs.pokemonexplorer.backend.remote.endpoints.TypeEndpoints
import com.sotirisapak.libs.pokemonexplorer.backend.remote.repositories.TypeRepository
import com.sotirisapak.libs.pokemonexplorer.core.models.ApiResult
import retrofit2.Retrofit

/**
 * Api service to implement [TypeEndpoints] methods
 * @param retrofit pass the retrofit instance ([PokemonApi.instance])
 * @author SotirisSapak
 * @since 1.0.0
 */
class TypeService(retrofit: Retrofit): TypeRepository {

    /** The api to reference in order to build the retrofit instance */
    val api = retrofit.create(TypeEndpoints::class.java)

    /**
     * Get only a specific pokemon type based on given type id
     * @param id the type id to fetch its data
     * @return an [ApiResult] object to hold two result states (Success and failure). In Success state
     * ([ApiResult.Success]) the result will be a [PokemonType]. In Failure state, the error message
     * @author SotirisSapak
     * @since 1.0.0
     */
    override suspend fun getTypeById(id: Int): ApiResult<PokemonType, String> {
        try {
            // we should properly execute the retrofit
            val result = api.getTypeById(id)
            // first, check the result if is successful or not
            if (result.isSuccessful && result.body() != null) {
                // return directly the result data
                return ApiResult.onSuccess(result.body() ?: PokemonType())
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