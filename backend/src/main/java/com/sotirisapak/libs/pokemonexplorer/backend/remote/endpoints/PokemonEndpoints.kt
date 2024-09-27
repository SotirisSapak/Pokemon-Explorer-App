package com.sotirisapak.libs.pokemonexplorer.backend.remote.endpoints

import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon
import com.sotirisapak.libs.pokemonexplorer.backend.models.PokemonType
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url

/**
 * Interface to reference the api endpoints for Pokemon
 * @author SotirisSapak
 * @since 1.0.0
 */
interface PokemonEndpoints {

    /**
     * Fetch pokemon list associated with a specific type.
     * @param url the endpoint url to fetch the pokemon. The url will be referenced from
     * [PokemonType.TypePokemon.PokemonInformation.url].
     * @return a new Retrofit [Response] with fetched [Pokemon] object
     * @author SotirisSapak
     * @since 1.0.0
     */
    @GET
    suspend fun getPokemon(@Url url: String): Response<Pokemon>

}