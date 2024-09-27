package com.sotirisapak.libs.pokemonexplorer.backend.remote.interfaces

import com.sotirisapak.libs.pokemonexplorer.backend.models.TypeModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TypeInterface {

    @GET("type/{id}")
    suspend fun getTypeById(@Path("id") id: Int): Response<TypeModel>

    @GET
    suspend fun getPokemonByUrl(@Path("url") url: String): Response<TypeModel>

}