package com.sotirisapak.apps.pokemonexplorer.views.favorites

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sotirisapak.apps.pokemonexplorer.adapters.PokemonAdapter
import com.sotirisapak.apps.pokemonexplorer.views.host.HostViewModel
import com.sotirisapak.libs.pokemonexplorer.backend.local.services.FavoritesService
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon
import com.sotirisapak.libs.pokemonexplorer.core.extensions.clear
import com.sotirisapak.libs.pokemonexplorer.core.extensions.mutableLiveData
import com.sotirisapak.libs.pokemonexplorer.core.extensions.onBackground
import com.sotirisapak.libs.pokemonexplorer.core.extensions.set
import com.sotirisapak.libs.pokemonexplorer.core.lifecycle.ViewModelBase
import com.sotirisapak.libs.pokemonexplorer.di.components.PokemonApplication

/**
 * ViewModel implementation for [FavoritesFragment]
 * @author SotirisSapak
 * @since 1.0.0
 */
class FavoritesViewModel(
    private val hostViewModel: HostViewModel,
    private val favoritesService: FavoritesService
): ViewModelBase() {

    val favoritesAdapter = PokemonAdapter(
        onPokemonClick = { _, pokemon ->
            hostViewModel.selectedPokemon = pokemon
            // trigger to navigate to preview page
            properties.proceed.set()
        },
        onPokemonLongClick =  { _, pokemon ->
            longClickedPokemon.set(pokemon)
            return@PokemonAdapter true
        }
    )

    private val items: MutableList<Pokemon> = mutableListOf()
    val itemsCount = 0.mutableLiveData
    val longClickedPokemon = Pokemon().mutableLiveData()

    init { getFavorites() }

    private fun getFavorites() = newJob(TAG_FETCH_FAVORITES) {
        items.clear()
        itemsCount.set(0)
        properties.progress.set()
        // run the fetch process
        var response: MutableList<Pokemon> = mutableListOf()
        onBackground { response = favoritesService.getFavorites().toMutableList() }
        // stop the progress as the fetch action finished
        properties.progress.clear()
        items.addAll(response)
        itemsCount.set(items.size)
        favoritesAdapter.submitList(response)
    }
    fun configureFavoriteSelectionOf(pokemon: Pokemon) = newJob(TAG_CONFIGURE_FAVORITE_POKEMON) {
        properties.progress.set()
        properties.error.clear()
        onBackground { favoritesService.deleteFromFavorites(pokemon) }
        refresh()
    }

    fun refresh() = getFavorites()

    companion object {

        const val TAG_FETCH_FAVORITES = "getFavorites"
        const val TAG_CONFIGURE_FAVORITE_POKEMON = "configureFavorite"

        fun factory(hostViewModel: HostViewModel) = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PokemonApplication)
                val favoritesService = application.getFavoritesService()
                FavoritesViewModel(hostViewModel, favoritesService)
            }
        }

    }

}