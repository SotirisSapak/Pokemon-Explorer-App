package com.sotirisapak.libs.pokemonexplorer.backend.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * The Pokemon api instance
 * @author SotirisSapak
 * @since 1.0.0
 */
object PokemonApi {

    /** The standard url for the api endpoint */
    private const val API_BASE_ENDPOINT = "https://pokeapi.co/api/v2/"

    /** The api instance */
    val instance: Retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(API_BASE_ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}