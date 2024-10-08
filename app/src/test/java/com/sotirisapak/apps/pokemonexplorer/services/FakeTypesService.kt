package com.sotirisapak.apps.pokemonexplorer.services

import com.sotirisapak.apps.pokemonexplorer.data.TypesData
import com.sotirisapak.libs.pokemonexplorer.backend.models.PokemonType
import com.sotirisapak.libs.pokemonexplorer.backend.remote.repositories.TypeRepository
import com.sotirisapak.libs.pokemonexplorer.core.models.ApiResult

/**
 * Fake types service to provide unit tests for viewModels
 * @author SotirisSapak
 * @since 1.0.0
 */
class FakeTypesService: TypeRepository {

    override suspend fun getTypeById(id: Int): ApiResult<PokemonType, String> = when(id) {
        9 -> ApiResult.onSuccess(TypesData.fetchedTypeSteel)
        10 -> ApiResult.onSuccess(TypesData.fetchedTypeFire)
        else -> ApiResult.onFailure("Cannot find type by id")
    }

}