package com.sotirisapak.libs.pokemonexplorer.di.components

import android.app.Application
import com.google.android.material.color.DynamicColors

/**
 * The master application component to provide dependencies to each viewModel
 * @author SotirisSapak
 * @since 1.0.0
 */
class PokemonApplication: Application() {


    /**
     * Initialize all dependencies here when application is about to be created
     */
    override fun onCreate() {
        super.onCreate()
        // enable Material3 dynamic theming if available
        DynamicColors.applyToActivitiesIfAvailable(this)
    }

}