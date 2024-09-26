package com.sotirisapak.libs.pokemonexplorer.backend.requests

import com.sotirisapak.libs.pokemonexplorer.core.models.ApiResult
import retrofit2.Response
import retrofit2.http.GET

interface Base {
    @GET
    fun basic(): Response<Boolean>
}