package com.sotirisapak.libs.pokemonexplorer.di.components

import android.app.Application
import com.google.android.material.color.DynamicColors
import com.sotirisapak.libs.pokemonexplorer.backend.PokemonApi
import com.sotirisapak.libs.pokemonexplorer.backend.remote.services.PokemonService
import com.sotirisapak.libs.pokemonexplorer.backend.remote.services.TypeService
import retrofit2.Retrofit

/**
 * The master application component to provide dependencies to each viewModel
 * @author SotirisSapak
 * @since 1.0.0
 */
class PokemonApplication: Application() {

    private lateinit var retrofit: Retrofit
    private lateinit var typeService: TypeService
    private lateinit var pokemonService: PokemonService

    /**
     * Initialize all dependencies here when application is about to be created
     */
    override fun onCreate() {
        super.onCreate()
        // enable Material3 dynamic theming if available
        DynamicColors.applyToActivitiesIfAvailable(this)
        // init retrofit instance
        retrofit = PokemonApi.instance
        // initialize all services
        typeService = TypeService(retrofit)
        pokemonService = PokemonService(retrofit)
    }

    fun getTypeService() = typeService
    fun getPokemonService() = pokemonService

}