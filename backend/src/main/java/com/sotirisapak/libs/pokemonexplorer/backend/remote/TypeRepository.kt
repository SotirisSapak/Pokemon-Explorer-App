package com.sotirisapak.libs.pokemonexplorer.backend.remote

import com.sotirisapak.libs.pokemonexplorer.backend.remote.interfaces.TypeInterface
import retrofit2.Retrofit

/**
 * The repository for pokemon types
 * @author SotirisSapak
 * @since 1.0.0
 */
class TypeRepository(private val retrofit: Retrofit) {

    /** The retrofit service instance */
    private val service = retrofit.create(TypeInterface::class.java)

    suspend fun getTypeById(id: Int) = service.getTypeById(id)
    suspend fun getPokemonByUrl(url: String) = service.getPokemonByUrl(url)

}