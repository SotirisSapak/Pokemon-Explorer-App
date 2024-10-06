package com.sotirisapak.apps.pokemonexplorer.views.preview

import android.util.Log
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sotirisapak.apps.pokemonexplorer.views.host.HostViewModel
import com.sotirisapak.libs.pokemonexplorer.backend.local.services.FavoritesService
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon
import com.sotirisapak.libs.pokemonexplorer.core.extensions.clear
import com.sotirisapak.libs.pokemonexplorer.core.extensions.mutableLiveData
import com.sotirisapak.libs.pokemonexplorer.core.extensions.onBackground
import com.sotirisapak.libs.pokemonexplorer.core.extensions.onUiThread
import com.sotirisapak.libs.pokemonexplorer.core.extensions.set
import com.sotirisapak.libs.pokemonexplorer.core.lifecycle.ViewModelBase
import com.sotirisapak.libs.pokemonexplorer.di.PokemonApplication

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
    private val favoritesService: FavoritesService
): ViewModelBase() {

    /**
     * The selected pokemon from previous page
     */
    val selectedPokemon = host.selectedPokemon

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

    init {
        fetchFavoriteState()
        Log.d("selected pokemon", selectedPokemon.toString())
    }

    /**
     * For this specific viewModel, we must override [onBackPressed] [ViewModelBase] method to
     * remove the selected pokemon from the host viewModel [HostViewModel].
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun onBackPressed(action: () -> Unit) {
        super.onBackPressed(action)
        // remove the selected pokemon from host viewModel
        host.selectedPokemon = Pokemon()
    }

    /**
     * __TAG__ -> [TAG_CONFIGURE_FAVORITE]
     *
     * Background task to make a pokemon favorite or not in offline database. This method will be called
     * from favorite button click via click listener.
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun onFavoriteSelection() = newJob(TAG_CONFIGURE_FAVORITE) {
        // reset viewModel properties
        properties.progress.set()
        properties.error.clear()
        // logic is simple! If this pokemon belongs to user's favorites, then remove from database, else
        // add it!
        onBackground {
            if(favoritesService.isFavorite(selectedPokemon)) {
                favoritesService.deleteFromFavorites(selectedPokemon)
                // cause is a LiveData we have to make this configuration to main thread
                onUiThread { isFavorite.set(false) }
            } else {
                favoritesService.insertToFavorites(selectedPokemon)
                // cause is a LiveData we have to make this configuration to main thread
                onUiThread { isFavorite.set(true) }
            }
        }
        properties.progress.clear()
        // configure the signal for favorite list cause user remove or add a pokemon to offline favorite database
        returnRefreshListSignal = true
    }

    /**
     * __TAG__ -> [TAG_FETCH_FAVORITE_STATE]
     *
     * Background task to fetch the pokemon favorite state (if is user's favorite or not)
     * @author SotirisSapak
     * @since 1.0.0
     */
    private fun fetchFavoriteState() = newJob(TAG_FETCH_FAVORITE_STATE) {
        properties.progress.set()
        properties.error.clear()
        onBackground {
            if(favoritesService.isFavorite(selectedPokemon)) {
                onUiThread { isFavorite.set(true) }
            } else {
                onUiThread { isFavorite.set(false) }
            }
        }
        properties.progress.clear()
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
    
}