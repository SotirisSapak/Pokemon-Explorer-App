package com.sotirisapak.apps.pokemonexplorer.views.preview

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sotirisapak.apps.pokemonexplorer.views.host.HostViewModel
import com.sotirisapak.libs.pokemonexplorer.backend.local.repositories.FavoritesRepository
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon
import com.sotirisapak.libs.pokemonexplorer.core.extensions.clear
import com.sotirisapak.libs.pokemonexplorer.core.extensions.mutableLiveData
import com.sotirisapak.libs.pokemonexplorer.core.extensions.set
import com.sotirisapak.libs.pokemonexplorer.core.lifecycle.ViewModelBase
import com.sotirisapak.libs.pokemonexplorer.di.PokemonApplication
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.annotations.TestOnly

/**
 * ViewModel implementation for [PreviewFragment]
 * @param host the [HostViewModel] instance to get the [HostViewModel.selectedPokemon] property
 * @param favoritesService api service to make this pokemon one of user's favorites or remove it from user's
 * favorites list
 * @author SotirisSapak
 * @since 1.0.0
 */
class PreviewViewModel(
    private val host: HostViewModel,
    private val favoritesService: FavoritesRepository
): ViewModelBase() {

    /**
     * The selected pokemon from previous page
     */
    var selectedPokemon = host.selectedPokemon

    /**
     * Indicator (LiveData) to configure the favorite state of the selected pokemon to save it to
     * local database for offline access
     */
    var isFavorite = false.mutableLiveData

    /**
     * Signal for the FavoriteList to refresh its list if user uncheck the favorite option of this
     * pokemon and this fragment is opened from the favorite list item selection.
     */
    var returnRefreshListSignal = false

    init { fetchFavoriteState() }

    /**
     * For this specific viewModel, we must override [onBackPressed] [ViewModelBase] method to
     * remove the selected pokemon from the host viewModel [HostViewModel].
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun onBackPressed(action: () -> Unit) {
        finishJobIfActive()
        // remove the selected pokemon from host viewModel
        host.selectedPokemon = Pokemon()
        action.invoke()
    }

    /**
     * __TAG__ -> [TAG_CONFIGURE_FAVORITE]
     *
     * __TEST READY!__
     *
     * Background task to make a pokemon favorite or not in offline database. This method will be called
     * from favorite button click via click listener.
     * @param dispatcher the dispatcher to execute the coroutine task (default is [Dispatchers.IO])
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun onFavoriteSelection(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        // reset viewModel properties
        properties.progress.set()
        properties.error.clear()
        finishJobIfActive()
        properties.job = viewModelScope.launch(dispatcher) {
            if(favoritesService.isFavorite(selectedPokemon)) {
                favoritesService.deleteFromFavorites(selectedPokemon)
                // cause is a LiveData we have to make this configuration to main thread
                isFavorite.postValue(false)
            } else {
                favoritesService.insertToFavorites(selectedPokemon)
                // cause is a LiveData we have to make this configuration to main thread
                isFavorite.postValue(true)
            }
            properties.progress.postValue(false)
            // configure the signal for favorite list cause user remove or add a pokemon to offline favorite database
            returnRefreshListSignal = true
        }
    }

    /**
     * __TAG__ -> [TAG_FETCH_FAVORITE_STATE]
     *
     * __TEST READY!__
     *
     * Background task to fetch the pokemon favorite state (if is user's favorite or not)
     * @param dispatcher the dispatcher to execute the coroutine task (default is [Dispatchers.IO])
     * @author SotirisSapak
     * @since 1.0.0
     */
    private fun fetchFavoriteState(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        properties.progress.set()
        properties.error.clear()
        finishJobIfActive()
        properties.job = viewModelScope.launch(dispatcher) {
            val result = favoritesService.isFavorite(selectedPokemon)
            isFavorite.postValue(result)
            properties.progress.postValue(false)
        }
    }

    companion object {

        const val TAG_CONFIGURE_FAVORITE = "configureFavOption"
        const val TAG_FETCH_FAVORITE_STATE = "fetchFavoriteState"

        fun factory(host: HostViewModel) = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PokemonApplication)
                val favoritesService = application.getFavoritesService()
                PreviewViewModel(host, favoritesService)
            }
        }

    }

    /**
     * Test function to be used only in test environment. This method must be set due to private scope
     * of [fetchFavoriteState]. This method is called in viewModel initialization and we have to test
     * it somehow!
     * @param dispatcher the dispatcher to execute the coroutine task (default is [Dispatchers.IO])
     */
    @TestOnly fun fetchFavoriteStateTest(dispatcher: CoroutineDispatcher = Dispatchers.IO) =
        fetchFavoriteState(dispatcher)

}