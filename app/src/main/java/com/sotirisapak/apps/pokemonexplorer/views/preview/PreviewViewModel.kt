package com.sotirisapak.apps.pokemonexplorer.views.preview

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sotirisapak.apps.pokemonexplorer.views.host.HostViewModel
import com.sotirisapak.libs.pokemonexplorer.backend.local.services.FavoritesService
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon
import com.sotirisapak.libs.pokemonexplorer.core.lifecycle.ViewModelBase
import com.sotirisapak.libs.pokemonexplorer.di.components.PokemonApplication

/**
 * ViewModel implementation for [PreviewFragment]
 * @author SotirisSapak
 * @since 1.0.0
 */
class PreviewViewModel(
    private val host: HostViewModel,
    private val favoritesService: FavoritesService
): ViewModelBase() {

    /** The pokemon that is selected from previous page */
    val selectedPokemon = host.selectedPokemon

    override fun onBackPressed(action: () -> Unit) {
        super.onBackPressed(action)
        // remove the selected pokemon from host viewModel
        host.selectedPokemon = Pokemon()
    }

    companion object {

        fun factory(host: HostViewModel) = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PokemonApplication)
                val favoritesService = application.getFavoritesService()
                PreviewViewModel(host, favoritesService)
            }
        }

    }
    
}