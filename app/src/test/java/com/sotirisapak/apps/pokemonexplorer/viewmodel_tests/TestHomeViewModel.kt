package com.sotirisapak.apps.pokemonexplorer.viewmodel_tests

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.sotirisapak.apps.pokemonexplorer.data.PokemonData
import com.sotirisapak.apps.pokemonexplorer.data.TypesData
import com.sotirisapak.apps.pokemonexplorer.models.Type
import com.sotirisapak.apps.pokemonexplorer.services.FakePokemonService
import com.sotirisapak.apps.pokemonexplorer.services.FakeTypesService
import com.sotirisapak.apps.pokemonexplorer.utilities.MainCoroutineRule
import com.sotirisapak.apps.pokemonexplorer.utilities.getOrAwaitValue
import com.sotirisapak.apps.pokemonexplorer.views.home.HomeViewModel
import com.sotirisapak.apps.pokemonexplorer.views.host.HostViewModel
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon
import com.sotirisapak.libs.pokemonexplorer.backend.remote.repositories.PokemonRepository
import com.sotirisapak.libs.pokemonexplorer.backend.remote.repositories.TypeRepository
import com.sotirisapak.libs.pokemonexplorer.core.extensions.clear
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

/**
 * Class to provide tests for [HomeViewModel]
 * @author SotirisSapak
 * @since 1.0.0
 */
@OptIn(ExperimentalCoroutinesApi::class)
class TestHomeViewModel {

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
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var typeService: TypeRepository
    private lateinit var pokemonService: PokemonRepository


    @Before fun setup() {
        hostViewModel = HostViewModel()
        pokemonService = FakePokemonService()
        typeService = FakeTypesService()
        homeViewModel = HomeViewModel(hostViewModel, typeService, pokemonService)
    }


    private fun resetViewModelProperties() {
        homeViewModel.properties.progress.clear()
        homeViewModel.properties.error.clear()
        homeViewModel.properties.proceed.clear()
        hostViewModel.selectedPokemon = Pokemon()
        homeViewModel.pokemonList.value = mutableListOf()
        homeViewModel.types.value = mutableListOf()
        homeViewModel.pokemonTypes.clear()
        homeViewModel.paginationProperties.index = 0
        homeViewModel.selectedPokemonType = Type()
    }


    @Test fun `Test - onInit`() = runTest {
        resetViewModelProperties()
        homeViewModel.onInit(coroutineRule.dispatcher)
        // check information
        // 2 actual pokemon list items (goto: TypesData.fetchedTypeSteel)
        assertEquals(2, homeViewModel.pokemonList.getOrAwaitValue().count())
        assertEquals(
            TypesData.fetchedTypeSteel.pokemonList[0].pokemonInformation.name,
            homeViewModel.pokemonList.getOrAwaitValue()[0].name
        )
        assertEquals(
            TypesData.fetchedTypeSteel.pokemonList[1].pokemonInformation.name,
            homeViewModel.pokemonList.getOrAwaitValue()[1].name
        )
        assertEquals(TypesData.typeSteelSelected, homeViewModel.selectedPokemonType)
        assertEquals("", homeViewModel.properties.error.getOrAwaitValue())
    }
    @Test fun `Test - onRefresh`() = runTest {
        resetViewModelProperties()
        homeViewModel.onInit(coroutineRule.dispatcher)
        // check information
        // 2 actual pokemon list items (goto: TypesData.fetchedTypeSteel)
        assertEquals(2, homeViewModel.pokemonList.getOrAwaitValue().count())
        assertEquals(
            TypesData.fetchedTypeSteel.pokemonList[0].pokemonInformation.name,
            homeViewModel.pokemonList.getOrAwaitValue()[0].name
        )
        assertEquals(
            TypesData.fetchedTypeSteel.pokemonList[1].pokemonInformation.name,
            homeViewModel.pokemonList.getOrAwaitValue()[1].name
        )
        assertEquals(TypesData.typeSteelSelected, homeViewModel.selectedPokemonType)
        assertEquals("", homeViewModel.properties.error.getOrAwaitValue())

        // refresh
        homeViewModel.refresh(coroutineRule.dispatcher)

        assertEquals(2, homeViewModel.pokemonList.getOrAwaitValue().count())
        assertEquals(
            TypesData.fetchedTypeSteel.pokemonList[0].pokemonInformation.name,
            homeViewModel.pokemonList.getOrAwaitValue()[0].name
        )
        assertEquals(
            TypesData.fetchedTypeSteel.pokemonList[1].pokemonInformation.name,
            homeViewModel.pokemonList.getOrAwaitValue()[1].name
        )
        assertEquals(TypesData.typeSteelSelected, homeViewModel.selectedPokemonType)
        assertEquals("", homeViewModel.properties.error.getOrAwaitValue())

    }
    @Test fun `Test - onTypeChange`() = runTest {
        resetViewModelProperties()
        homeViewModel.onInit(coroutineRule.dispatcher)
        // change type
        homeViewModel.onTypeClick(coroutineRule.dispatcher, TypesData.typeFire)

        // assertEquals(2, homeViewModel.pokemonList.getOrAwaitValue().count())
        assertEquals("pokemon3", homeViewModel.pokemonList.getOrAwaitValue()[0].name)
        assertEquals(TypesData.typeFire, homeViewModel.selectedPokemonType)
        assertEquals("", homeViewModel.properties.error.getOrAwaitValue())
        assertEquals(2, homeViewModel.pokemonList.getOrAwaitValue().count())
    }
    @Test fun `Test - onPokemonClick`() = runTest {
        resetViewModelProperties()
        homeViewModel.onInit(coroutineRule.dispatcher)
        // click on a pokemon
        homeViewModel.onPokemonClick(homeViewModel.pokemonList.getOrAwaitValue()[0])
        assertEquals(PokemonData.pokemon1, hostViewModel.selectedPokemon)
        assertEquals(true, homeViewModel.properties.proceed.getOrAwaitValue())
    }
    @Test fun `Test - onLoadMore`() = runTest {
        resetViewModelProperties()
        homeViewModel.onInit(coroutineRule.dispatcher)
        // check information
        // 2 actual pokemon list items (goto: TypesData.fetchedTypeSteel)
        assertEquals(2, homeViewModel.pokemonList.getOrAwaitValue().count())
        assertEquals(
            TypesData.fetchedTypeSteel.pokemonList[0].pokemonInformation.name,
            homeViewModel.pokemonList.getOrAwaitValue()[0].name
        )
        assertEquals(
            TypesData.fetchedTypeSteel.pokemonList[1].pokemonInformation.name,
            homeViewModel.pokemonList.getOrAwaitValue()[1].name
        )
        assertEquals(TypesData.typeSteelSelected, homeViewModel.selectedPokemonType)
        assertEquals("", homeViewModel.properties.error.getOrAwaitValue())

        // load more
        homeViewModel.loadMoreData(coroutineRule.dispatcher)
        assertEquals(2, homeViewModel.pokemonList.getOrAwaitValue().count())
        assertEquals(TypesData.typeSteelSelected, homeViewModel.selectedPokemonType)
        assertEquals("", homeViewModel.properties.error.getOrAwaitValue())
    }
    @Test fun `Test - onLoadMoreNoPreviousItems`() = runTest {
        resetViewModelProperties()
        homeViewModel.onInit(coroutineRule.dispatcher)
        // check information
        // 2 actual pokemon list items (goto: TypesData.fetchedTypeSteel)
        assertEquals(2, homeViewModel.pokemonList.getOrAwaitValue().count())
        assertEquals(
            TypesData.fetchedTypeSteel.pokemonList[0].pokemonInformation.name,
            homeViewModel.pokemonList.getOrAwaitValue()[0].name
        )
        assertEquals(
            TypesData.fetchedTypeSteel.pokemonList[1].pokemonInformation.name,
            homeViewModel.pokemonList.getOrAwaitValue()[1].name
        )
        assertEquals(TypesData.typeSteelSelected, homeViewModel.selectedPokemonType)
        assertEquals("", homeViewModel.properties.error.getOrAwaitValue())

        // delete items from list
        homeViewModel.pokemonList.value = mutableListOf()

        // load more
        homeViewModel.loadMoreData(coroutineRule.dispatcher)
        assertEquals(0, homeViewModel.pokemonList.getOrAwaitValue().count())
        assertEquals(TypesData.typeSteelSelected, homeViewModel.selectedPokemonType)
        assertEquals("", homeViewModel.properties.error.getOrAwaitValue())
    }
    @Test fun `Test - onGoBackFromFavorites-FullList`() = runTest {
        resetViewModelProperties()
        homeViewModel.onInit(coroutineRule.dispatcher)
        // check information
        // 2 actual pokemon list items (goto: TypesData.fetchedTypeSteel)
        assertEquals(2, homeViewModel.pokemonList.getOrAwaitValue().count())
        assertEquals(
            TypesData.fetchedTypeSteel.pokemonList[0].pokemonInformation.name,
            homeViewModel.pokemonList.getOrAwaitValue()[0].name
        )
        assertEquals(
            TypesData.fetchedTypeSteel.pokemonList[1].pokemonInformation.name,
            homeViewModel.pokemonList.getOrAwaitValue()[1].name
        )
        assertEquals(TypesData.typeSteelSelected, homeViewModel.selectedPokemonType)
        assertEquals("", homeViewModel.properties.error.getOrAwaitValue())

        // delete items from list
        homeViewModel.refreshIfEmpty(coroutineRule.dispatcher)
        assertEquals(2, homeViewModel.pokemonList.getOrAwaitValue().count())
        assertEquals(TypesData.typeSteelSelected, homeViewModel.selectedPokemonType)
        assertEquals("", homeViewModel.properties.error.getOrAwaitValue())
    }
    @Test fun `Test - onGoBackFromFavorites-EmptyList`() = runTest {
        resetViewModelProperties()
        homeViewModel.onInit(coroutineRule.dispatcher)
        // check information
        // 2 actual pokemon list items (goto: TypesData.fetchedTypeSteel)
        assertEquals(2, homeViewModel.pokemonList.getOrAwaitValue().count())
        assertEquals(
            TypesData.fetchedTypeSteel.pokemonList[0].pokemonInformation.name,
            homeViewModel.pokemonList.getOrAwaitValue()[0].name
        )
        assertEquals(
            TypesData.fetchedTypeSteel.pokemonList[1].pokemonInformation.name,
            homeViewModel.pokemonList.getOrAwaitValue()[1].name
        )
        assertEquals(TypesData.typeSteelSelected, homeViewModel.selectedPokemonType)
        assertEquals("", homeViewModel.properties.error.getOrAwaitValue())

        // delete items from list
        homeViewModel.pokemonList.value = mutableListOf()
        assertEquals(0, homeViewModel.pokemonList.getOrAwaitValue().count())
        assertEquals(TypesData.typeSteelSelected, homeViewModel.selectedPokemonType)

        // refresh list
        homeViewModel.refreshIfEmpty(coroutineRule.dispatcher)

        assertEquals(2, homeViewModel.pokemonList.getOrAwaitValue().count())
        assertEquals(TypesData.typeSteelSelected, homeViewModel.selectedPokemonType)
        assertEquals("", homeViewModel.properties.error.getOrAwaitValue())
    }
    @Test fun `Test - TypeError`() = runTest {
        resetViewModelProperties()
        homeViewModel.types.value = mutableListOf(TypesData.typeUnknown)
        homeViewModel.loadTypes(coroutineRule.dispatcher)
        assert(homeViewModel.properties.error.getOrAwaitValue().isNotEmpty()) {
            "Cannot find type by id"
        }
        assertEquals(0, homeViewModel.pokemonList.getOrAwaitValue().count())
    }

}