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
 * @param hostViewModel the viewModel instance of [HostViewModel] in order to manipulate [HostViewModel.selectedPokemon]
 * attribute.
 * @param favoritesService the local database service to manipulate favorites list.
 * @author SotirisSapak
 * @since 1.0.0
 */
class FavoritesViewModel(
    private val hostViewModel: HostViewModel,
    private val favoritesService: FavoritesService
): ViewModelBase() {

    /**
     * [PokemonAdapter] for fragment's recyclerView to show all favorites as standard pokemon
     * list so as to have consistency.
     */
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

    /**
     * The favorites list as property
     */
    private val items: MutableList<Pokemon> = mutableListOf()

    /**
     * The items of the favorites list as count. This liveData will be used from ui, in order to proper
     * manage states (empty state and result state)
     */
    val itemsCount = 0.mutableLiveData

    /**
     * Trigger that will be observed from [FavoritesFragment] to remove an item from favorites list
     * via long click. When user long click an item from favorites list, then this item will be removed
     * from favorites. Need to have a trigger cause if we do all this process within viewModel, compiler
     * will throw a recursive error. So, viewModel trigger a long click item signal and view will
     * trigger the list refresh.
     */
    val longClickedPokemon = Pokemon().mutableLiveData()

    init {
        // fetch favorites list on viewModel initialization
        getFavorites()
    }

    /**
     * __TAG__ -> [TAG_FETCH_FAVORITES]
     *
     * Background task to get favorites list from local database
     * @author SotirisSapak
     * @since 1.0.0
     */
    private fun getFavorites() = newJob(TAG_FETCH_FAVORITES) {
        // reset viewModel properties
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

    /**
     * __TAG__ -> [TAG_CONFIGURE_FAVORITE_POKEMON]
     *
     * Method to be called when a user long click an item from favorites list. This method will remove
     * given [pokemon] from favorites list and trigger also a list refresh to show the deletion result.
     * @param pokemon the pokemon to delete from favorites
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun configureFavoriteSelectionOf(pokemon: Pokemon) = newJob(TAG_CONFIGURE_FAVORITE_POKEMON) {
        properties.progress.set()
        properties.error.clear()
        onBackground { favoritesService.deleteFromFavorites(pokemon) }
        refresh()
    }

    /**
     * Refresh list by calling [getFavorites] method
     * @author SotirisSapak
     * @since 1.0.0
     */
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