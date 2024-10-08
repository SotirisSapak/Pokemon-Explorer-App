package com.sotirisapak.apps.pokemonexplorer.services

import com.sotirisapak.apps.pokemonexplorer.data.PokemonData
import com.sotirisapak.apps.pokemonexplorer.data.TypesData
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon
import com.sotirisapak.libs.pokemonexplorer.backend.remote.repositories.PokemonRepository
import com.sotirisapak.libs.pokemonexplorer.core.models.ApiResult

/**
 * Fake pokemon service to provide unit tests for viewModels
 * @author SotirisSapak
 * @since 1.0.0
 */
class FakePokemonService: PokemonRepository {

    override suspend fun getPokemonByUrl(endpointUrl: String): ApiResult<Pokemon, String> = when(endpointUrl) {
        TypesData.fetchedTypeSteel.pokemonList[0].pokemonInformation.url -> ApiResult.onSuccess(PokemonData.pokemon1)
        TypesData.fetchedTypeSteel.pokemonList[1].pokemonInformation.url -> ApiResult.onSuccess(PokemonData.pokemon2)
        TypesData.fetchedTypeFire.pokemonList[0].pokemonInformation.url -> ApiResult.onSuccess(PokemonData.pokemon3)
        TypesData.fetchedTypeFire.pokemonList[1].pokemonInformation.url -> ApiResult.onSuccess(PokemonData.pokemon4)
        else -> ApiResult.onFailure("Endpoint is not in list")
    }

}