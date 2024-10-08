package com.sotirisapak.apps.pokemonexplorer.viewmodel_tests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sotirisapak.apps.pokemonexplorer.data.PokemonData
import com.sotirisapak.apps.pokemonexplorer.services.FakeFavoritesService
import com.sotirisapak.apps.pokemonexplorer.utilities.MainCoroutineRule
import com.sotirisapak.apps.pokemonexplorer.utilities.getOrAwaitValue
import com.sotirisapak.apps.pokemonexplorer.views.favorites.FavoritesViewModel
import com.sotirisapak.apps.pokemonexplorer.views.host.HostViewModel
import com.sotirisapak.libs.pokemonexplorer.backend.local.repositories.FavoritesRepository
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class TestFavoritesViewModel {

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
    private lateinit var viewModel: FavoritesViewModel

    @Before
    fun setup() {
        hostViewModel = HostViewModel()
        favoritesService = FakeFavoritesService()
        viewModel = FavoritesViewModel(hostViewModel, favoritesService)
    }

    @Test fun `viewModel - onInit - no favorites`() = runTest {
        // remove all favorites from list
        for(items in favoritesService.getFavorites()) { favoritesService.deleteFromFavorites(items) }
        viewModel.getFavorites(coroutineRule.dispatcher)
        assertEquals(0, viewModel.itemsCount.getOrAwaitValue())
        assertEquals(0, viewModel.items.getOrAwaitValue().size)
        // reset attributes
        viewModel.itemsCount.postValue(0)
        viewModel.items.postValue(mutableListOf())
    }
    @Test fun `viewModel - onInit - with favorites`() = runTest {
        // add some favorites to list
        favoritesService.insertToFavorites(PokemonData.pokemon1)
        favoritesService.insertToFavorites(PokemonData.pokemon2)
        viewModel.getFavorites(coroutineRule.dispatcher)
        assertEquals(2, viewModel.itemsCount.getOrAwaitValue())
        assertEquals(2, viewModel.items.getOrAwaitValue().size)

        // reset attributes
        viewModel.itemsCount.postValue(0)
        viewModel.items.postValue(mutableListOf())
        for(items in favoritesService.getFavorites()) { favoritesService.deleteFromFavorites(items) }
    }
    @Test fun `viewModel - onDelete`() = runTest {
        // add some favorites to list
        favoritesService.insertToFavorites(PokemonData.pokemon1)
        favoritesService.insertToFavorites(PokemonData.pokemon2)
        viewModel.getFavorites(coroutineRule.dispatcher)
        // remove pokemon1 from local storage
        viewModel.onPokemonLongClick(PokemonData.pokemon1)
        assertEquals(PokemonData.pokemon1, viewModel.longClickedPokemon.getOrAwaitValue())
        viewModel.configureFavoriteSelectionOf(coroutineRule.dispatcher, PokemonData.pokemon1)
        assertEquals(1, viewModel.itemsCount.getOrAwaitValue())
        assertEquals(1, viewModel.items.getOrAwaitValue().size)
        assertEquals(PokemonData.pokemon2, viewModel.items.getOrAwaitValue()[0])

        // reset attributes
        viewModel.itemsCount.postValue(0)
        viewModel.items.postValue(mutableListOf())
        for(items in favoritesService.getFavorites()) { favoritesService.deleteFromFavorites(items) }
    }
    @Test fun `viewModel - onRefresh`() = runTest {
        // add some favorites to list
        favoritesService.insertToFavorites(PokemonData.pokemon1)
        favoritesService.insertToFavorites(PokemonData.pokemon2)
        viewModel.getFavorites(coroutineRule.dispatcher)
        assertEquals(2, viewModel.itemsCount.getOrAwaitValue())
        assertEquals(2, viewModel.items.getOrAwaitValue().size)
        favoritesService.insertToFavorites(PokemonData.pokemon3)
        viewModel.refresh(coroutineRule.dispatcher)
        assertEquals(3, viewModel.itemsCount.getOrAwaitValue())
        assertEquals(3, viewModel.items.getOrAwaitValue().size)

        // reset attributes
        viewModel.itemsCount.postValue(0)
        viewModel.items.postValue(mutableListOf())
        for(items in favoritesService.getFavorites()) { favoritesService.deleteFromFavorites(items) }
    }
    @Test fun `viewModel - onPokemonClick`() = runTest {
        viewModel.onPokemonClick(PokemonData.pokemon1)
        assertEquals(PokemonData.pokemon1, hostViewModel.selectedPokemon)
    }

}