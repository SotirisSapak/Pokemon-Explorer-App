package com.sotirisapak.libs.pokemonexplorer.backend.remote.endpoints

import com.sotirisapak.libs.pokemonexplorer.backend.models.PokemonType
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path


/**
 * Interface to reference the api endpoints for Types
 * @author SotirisSapak
 * @since 1.0.0
 */
interface TypeEndpoints {

    /**
     * Get only a specific pokemon type based on given type id
     * @param id the type id to fetch its data
     * @return a new Retrofit [Response] with fetched [PokemonType]
     * @author SotirisSapak
     * @since 1.0.0
     */
    @GET("type/{id}")
    suspend fun getTypeById(@Path("id") id: Int): Response<PokemonType>

}