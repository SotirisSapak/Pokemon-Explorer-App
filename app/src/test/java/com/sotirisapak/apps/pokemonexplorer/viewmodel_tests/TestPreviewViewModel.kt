package com.sotirisapak.apps.pokemonexplorer.viewmodel_tests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sotirisapak.apps.pokemonexplorer.data.FavoritesData
import com.sotirisapak.apps.pokemonexplorer.services.FakeFavoritesService
import com.sotirisapak.apps.pokemonexplorer.utilities.MainCoroutineRule
import com.sotirisapak.apps.pokemonexplorer.utilities.getOrAwaitValue
import com.sotirisapak.apps.pokemonexplorer.views.host.HostViewModel
import com.sotirisapak.apps.pokemonexplorer.views.preview.PreviewViewModel
import com.sotirisapak.libs.pokemonexplorer.backend.local.repositories.FavoritesRepository
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon
import com.sotirisapak.libs.pokemonexplorer.core.extensions.clear
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Class to provide tests for [PreviewViewModel]
 * @author SotirisSapak
 * @since 1.0.0
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TestPreviewViewModel {

    /**
     * A unit test rule to make dispatcher run these tasks on a single thread even if coroutine run
     * its body in another thread
     */
    @get:Rule
    val rule = InstantTaskExecutorRule()

    /**
     * A rule for coroutine trigger. Useful to get the dispatcher by calling [MainCoroutineRule.dispatcher]
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val coroutineRule = MainCoroutineRule()


    private lateinit var hostViewModel: HostViewModel
    private lateinit var favoritesService: FavoritesRepository
    private lateinit var viewModel: PreviewViewModel

    @Before fun setup() {
        hostViewModel = HostViewModel()
        favoritesService = FakeFavoritesService()
        viewModel = PreviewViewModel(hostViewModel, favoritesService)
    }

    @Test fun test_viewModelInit_isNoFavorite() = runTest {
        viewModel.isFavorite.clear()
        viewModel.selectedPokemon = FavoritesData.pokemon1
        viewModel.fetchFavoriteStateTest(coroutineRule.dispatcher)
        assert(!viewModel.isFavorite.getOrAwaitValue())

        // clear affected properties
        viewModel.isFavorite.clear()
        viewModel.selectedPokemon = Pokemon()
    }
    @Test fun test_viewModelInit_isFavorite() = runTest {
        viewModel.isFavorite.clear()
        viewModel.selectedPokemon = FavoritesData.pokemon1
        viewModel.onFavoriteSelection(coroutineRule.dispatcher)
        viewModel.fetchFavoriteStateTest(coroutineRule.dispatcher)
        assert(viewModel.isFavorite.getOrAwaitValue())

        // clear affected properties
        viewModel.isFavorite.clear()
        viewModel.selectedPokemon = Pokemon()
        favoritesService.deleteFromFavorites(viewModel.selectedPokemon)
    }
    @Test fun test_makePokemonFavorite() = runTest {
        viewModel.isFavorite.clear()
        viewModel.selectedPokemon = FavoritesData.pokemon1
        favoritesService.deleteFromFavorites(FavoritesData.pokemon1)
        viewModel.onFavoriteSelection(coroutineRule.dispatcher)
        assert(viewModel.isFavorite.getOrAwaitValue())

        // clear affected properties
        viewModel.isFavorite.clear()
        viewModel.selectedPokemon = Pokemon()
        favoritesService.deleteFromFavorites(FavoritesData.pokemon1)
    }
    @Test fun test_removePokemonFromFavorite() = runTest {
        viewModel.isFavorite.clear()
        viewModel.selectedPokemon = FavoritesData.pokemon1
        favoritesService.insertToFavorites(viewModel.selectedPokemon)
        viewModel.onFavoriteSelection(coroutineRule.dispatcher)
        assert(!viewModel.isFavorite.getOrAwaitValue())
        assertEquals(0, favoritesService.getFavorites().size)

        // clear affected properties
        viewModel.isFavorite.clear()
        viewModel.selectedPokemon = Pokemon()
    }
    @Test fun test_onFavoritesSelectionRefreshListTrigger() = runTest {
        viewModel.isFavorite.clear()
        viewModel.selectedPokemon = FavoritesData.pokemon1
        viewModel.onFavoriteSelection(coroutineRule.dispatcher)
        assert(viewModel.returnRefreshListSignal)
        // clear affected properties
        viewModel.isFavorite.clear()
        viewModel.selectedPokemon = Pokemon()
        favoritesService.deleteFromFavorites(FavoritesData.pokemon1)
    }

}