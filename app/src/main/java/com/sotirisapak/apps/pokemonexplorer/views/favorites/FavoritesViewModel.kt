package com.sotirisapak.apps.pokemonexplorer.views.favorites

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sotirisapak.apps.pokemonexplorer.views.host.HostViewModel
import com.sotirisapak.libs.pokemonexplorer.backend.local.repositories.FavoritesRepository
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon
import com.sotirisapak.libs.pokemonexplorer.core.extensions.mutableLiveData
import com.sotirisapak.libs.pokemonexplorer.core.extensions.set
import com.sotirisapak.libs.pokemonexplorer.core.lifecycle.ViewModelBase
import com.sotirisapak.libs.pokemonexplorer.di.PokemonApplication
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel implementation for [FavoritesFragment]
 * @param hostViewModel the viewModel instance of [HostViewModel] in order to manipulate [HostViewModel.selectedPokemon]
 * attribute.
 * @param favoritesRepository the local database service to manipulate favorites list.
 * @author SotirisSapak
 * @since 1.0.0
 */
class FavoritesViewModel(
    private val hostViewModel: HostViewModel,
    private val favoritesRepository: FavoritesRepository
): ViewModelBase() {

    /**
     * The favorites list as property
     */
    val items: MutableLiveData<MutableList<Pokemon>> = MutableLiveData(mutableListOf())

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
        properties.progress.set()
        // fetch favorites list on viewModel initialization
        getFavorites()
    }

    /**
     * __TAG__ -> [TAG_FETCH_FAVORITES]
     *
     * __TEST READY__
     *
     * Background task to get favorites list from local database
     * @param dispatcher the dispatcher to execute the coroutine task (default is [Dispatchers.IO])
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun getFavorites(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        // reset viewModel properties
        finishJobIfActive()
        properties.progress.postValue(true)
        items.postValue(emptyList<Pokemon>().toMutableList())
        itemsCount.postValue(0)
        var response: MutableList<Pokemon>
        properties.job = viewModelScope.launch(dispatcher) {
            response = favoritesRepository.getFavorites().toMutableList()
            // stop the progress as the fetch action finished
            properties.progress.postValue(false)
            items.postValue(response)
            itemsCount.postValue(response.size)
        }
    }

    /**
     * __TAG__ -> [TAG_CONFIGURE_FAVORITE_POKEMON]
     *
     * __TEST READY__
     *
     * Method to be called when a user long click an item from favorites list. This method will remove
     * given [pokemon] from favorites list and trigger also a list refresh to show the deletion result.
     * @param pokemon the pokemon to delete from favorites
     * @param dispatcher the dispatcher to execute the coroutine task (default is [Dispatchers.IO])
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun configureFavoriteSelectionOf(dispatcher: CoroutineDispatcher = Dispatchers.IO, pokemon: Pokemon) {
        properties.progress.postValue(true)
        properties.error.postValue("")
        finishJobIfActive()
        properties.job = viewModelScope.launch(dispatcher) {
            favoritesRepository.deleteFromFavorites(pokemon)
            refresh(dispatcher)
        }
    }

    /**
     *
     * __TEST READY__
     *
     * Refresh list by calling [getFavorites] method
     * @param dispatcher the dispatcher to execute the coroutine task (default is [Dispatchers.IO])
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun refresh(dispatcher: CoroutineDispatcher = Dispatchers.IO) = getFavorites(dispatcher)

    /**
     * Click interaction for pokemon adapter to bind this action when user click at a pokemon
     * @param pokemon the pokemon instance that user clicked
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun onPokemonClick(pokemon: Pokemon) {
        hostViewModel.selectedPokemon = pokemon
        // trigger to navigate to preview page
        properties.proceed.postValue(true)
    }

    /**
     * Long click interaction for pokemon adapter to bind this action when user long click at a pokemon
     * @param pokemon the pokemon instance that user long clicked
     * @return true if the event was consumed
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun onPokemonLongClick(pokemon: Pokemon): Boolean {
        longClickedPokemon.postValue(pokemon)
        return true
    }

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