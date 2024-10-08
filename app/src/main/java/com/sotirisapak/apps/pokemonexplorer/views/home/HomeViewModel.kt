package com.sotirisapak.apps.pokemonexplorer.views.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sotirisapak.apps.pokemonexplorer.adapters.PokemonAdapter
import com.sotirisapak.apps.pokemonexplorer.adapters.TypeAdapter
import com.sotirisapak.apps.pokemonexplorer.models.Type
import com.sotirisapak.apps.pokemonexplorer.views.host.HostViewModel
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon
import com.sotirisapak.libs.pokemonexplorer.backend.models.PokemonType
import com.sotirisapak.libs.pokemonexplorer.backend.remote.repositories.PokemonRepository
import com.sotirisapak.libs.pokemonexplorer.backend.remote.repositories.TypeRepository
import com.sotirisapak.libs.pokemonexplorer.core.extensions.clear
import com.sotirisapak.libs.pokemonexplorer.core.extensions.nonNullValue
import com.sotirisapak.libs.pokemonexplorer.core.extensions.set
import com.sotirisapak.libs.pokemonexplorer.core.lifecycle.ViewModelBase
import com.sotirisapak.libs.pokemonexplorer.core.models.ApiResult
import com.sotirisapak.libs.pokemonexplorer.di.PokemonApplication
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * ViewModel implementation for [HomeFragment]
 * @param hostViewModel the [HostViewModel] instance to manipulate [HostViewModel.selectedPokemon] property
 * @param typeRepository the api service to provide methods to manage pokemon types
 * @param pokemonRepository the api service to provide methods to manage pokemon
 * @author SotirisSapak
 * @since 1.0.0
 */
class HomeViewModel(
    private val hostViewModel: HostViewModel,
    private val typeRepository: TypeRepository,
    private val pokemonRepository: PokemonRepository
): ViewModelBase() {

    // ? ==========================================================================================
    // ? Nested classes for better organization
    // ? ==========================================================================================

    /**
     * Class to provide pagination attributes
     * @author SotirisSapak
     * @since 1.0.0
     */
    inner class Pagination {

        /**
         * Offset is the amount of items to fetch next. Default is 15
         */
        var offset = 15

        /**
         * Index is the indicator to tell the process at which count is.
         *
         * Example:
         *
         * <code>
         *
         *     ---------------------------------
         *     total: 240 items
         *     ---------------------------------
         *     index: 0
         *     offset: 20
         *
         *     -> fetch 20 first items
         *     -> index += offset (20)
         *     ---------------------------------
         *     index: 20
         *     offset: 20
         *
         *     -> fetch next 20 items
         *     -> index += offset (40)
         * </code>
         */
        var index = 0

    }

    // ? ==========================================================================================
    // ? ViewModel properties (variables)
    // ? ==========================================================================================

    /**
     * The [Type] instances to bind into recyclerView
     */
    val types = MutableLiveData<MutableList<Type>>()

    /**
     * The pokemon list as standard information in a private list. This list, will be used to hold all the pokemon
     * fetched from the selected type, before any pagination applied and actual pokemon fetching via
     * [PokemonType.TypePokemon.PokemonInformation.url]
     */
    val pokemonTypes = mutableListOf<PokemonType.TypePokemon>()

    /**
     * The list of pokemon to hold all the fetched data from api. This list will hold all pokemon
     * and with the use of pagination technique, will save only a small junk of data
     */
    val pokemonList = MutableLiveData<MutableList<Pokemon>>()

    /**
     * Variable to hold a state for the type adapter to prevent reload current selected type its data.
     * @see TypeAdapter
     */
    var selectedPokemonType = Type()

    // ? ------------------------
    // ? Class instances
    // ? ------------------------

    /**
     * Instance of [Pagination] class
     */
    val paginationProperties = Pagination()

    // ? ==========================================================================================
    // ? Ui thread methods
    // ? ==========================================================================================

    init { onInit() }

    /**
     *
     * __TEST READY__
     *
     * We need to set firstly, the 10 most famous categories of pokemon. These categories will
     * be saved as [Type] which is a standard model for pokemon categories before fetching its
     * data which will be saved as [PokemonType] objects.
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun onInit(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        val arrayOfTypes = mutableListOf(
            Type(9, "Steel", true),
            Type(10, "Fire"),
            Type(11, "Water"),
            Type(12, "Grass"),
            Type(13, "Electric"),
            Type(14, "Psychic"),
            Type(16, "Dragon"),
            Type(17, "Dark"),
            Type(18, "Fairy")
        )
        // clear lists just in case!
        // types.value = mutableListOf()
        // pokemonList.value = mutableListOf()

        // attach the standard types to show as options within the recyclerView via
        // dataBinding
        types.value = arrayOfTypes
        // Now, we have to fetch the data for the selected type which is the "Steel".
        selectedPokemonType = arrayOfTypes[0]
        // fetch all data for "Steel"
        loadTypes(dispatcher)
    }

    /**
     *
     * __TEST READY__
     *
     * Click listener for [TypeAdapter] interaction to trigger an action when user click on a pokemon
     * type.
     * @param selectedType the [Type] instance to be clicked
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun onTypeClick(dispatcher: CoroutineDispatcher = Dispatchers.IO, selectedType: Type) {
        // to avoid re-creating adapter when the already selected list is clicked again,
        // we should make program remember if the selected type is the one which is clicked
        if(selectedPokemonType != selectedType) {
            selectedPokemonType = selectedType
            finishJobIfActive()
            loadTypes(dispatcher)
        }
    }


    /**
     * Click listener for [PokemonAdapter] interaction to trigger an action when user click on a pokemon
     * from list.
     * @param clickedPokemon the [Pokemon] instance to be clicked
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun onPokemonClick(clickedPokemon: Pokemon){
        hostViewModel.selectedPokemon = clickedPokemon
        finishJobIfActive()
        // trigger a signal to view to go to preview fragment
        properties.proceed.set()
    }

    // ? ==========================================================================================
    // ? Background tasks
    // ? ==========================================================================================

    /**
     * __TAG__ -> [TAG_FETCH_SELECTED_TYPE_DATA]
     *
     * __TEST READY__
     *
     * Method to fetch the data of the selected type. No need to pass a parameter cause the [selectedPokemonType]
     * will be used to fetch its data.
     *
     * Hint: This method will use the [newJob] method to generate a new coroutine job within [viewModelScope].
     *
     * @param dispatcher the dispatcher to execute the coroutine task (default is [Dispatchers.IO])
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun loadTypes(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        // remove job if is open
        finishJobIfActive()
        // reset progress and errors
        properties.progress.set()
        properties.error.clear()
        // reset pagination index
        paginationProperties.index = 0
        // reset pokemon list and adapter cause we have selected a new type
        pokemonList.value = mutableListOf()
        // start the process
        properties.job = viewModelScope.launch(dispatcher) {
            when(val response = typeRepository.getTypeById(selectedPokemonType.id)) {
                is ApiResult.Failure -> {
                    properties.progress.postValue(false)
                    properties.error.postValue(response.error)
                }
                is ApiResult.Success -> {
                    // check if response return an empty list
                    if(response.data.pokemonList.isEmpty()) {
                        // stop the process
                        properties.progress.postValue(false)
                        properties.error.postValue("Unknown reason for not fetching pokemon for type \"${selectedPokemonType.name}\"")
                    } else {
                        // continue by fetching pokemon information
                        pokemonTypes.clear()
                        pokemonTypes.addAll(response.data.pokemonList)
                        fetchPokemonList()
                    }
                }
            }
        }
    }

    /**
     * __TAG__ -> [TAG_FETCH_POKEMON]
     *
     * In order to execute this method properly, you need to already have a configured [pokemonTypes]
     * list with all the [PokemonType.TypePokemon] information to get the [PokemonType.TypePokemon.PokemonInformation.url]
     * and fetch the information about pokemon. This method, by default, will fetch pokemon list
     * with pagination, based on [Pagination] class properties: [Pagination.index] and [Pagination.offset].
     *
     *
     * @author SotirisSapak
     * @since 1.0.0
     */

    private suspend fun fetchPokemonList() {
        // configure viewModel properties
        properties.progress.postValue(true)
        properties.error.postValue("")
        // configure pagination before executing
        val paginatedList = configurePagination(pokemonTypes)
        // create a new list to add the result
        val resultList = mutableListOf<Pokemon>()
        // begin execution
        paginatedList.forEach { item ->
            when(val response = pokemonRepository.getPokemonByUrl(item.pokemonInformation.url)) {
                is ApiResult.Failure -> { /* ignore this pokemon */ }
                is ApiResult.Success -> { resultList.add(response.data) }
            }
        }
        // check if the result list is empty
        if(resultList.isNotEmpty()) {
            // submit fetched list to liveData list to trigger the view observer to bind the adapter
            val previousListItems = pokemonList.value?.toMutableList() ?: mutableListOf()
            previousListItems.addAll(resultList)
            pokemonList.postValue(previousListItems)
        }
        // stop the process
        properties.progress.postValue(false)
    }


    /**
     * *(Tried to extract this method as [List] extension with T as generic but when pagination applied
     * a weird recyclerView animation appeared so I leave code as is!)*
     *
     * Task to configure pagination indexes and list items to fetch only those
     * @param listToPaginate the list of items to use in pagination.
     * @return the paginated list by [Pagination.index] and [Pagination.offset]
     * @author SotirisSapak
     * @since 1.0.0
     */
    private fun <T> configurePagination(listToPaginate: List<T>): List<T> {
        // create a new list to use in pagination as result
        val resultList = mutableListOf<T>()
        // counter for the pagination loop process to stop when loop index stops at pagination offset
        var count = 0
        // begin pagination
        for(i in paginationProperties.index..<listToPaginate.count()) {
            resultList.add(listToPaginate[i])
            count++
            paginationProperties.index++
            if(count == paginationProperties.offset) break
        }
        return resultList
    }

    /**
     * __TAG__ -> [TAG_FETCH_MORE_POKEMON]
     *
     * __TEST READY__
     *
     * When user reaches the end of the list, this method will be triggered in order to fetch more
     * pokemon filter by selected type using pagination
     * @param dispatcher the dispatcher to execute the coroutine task (default is [Dispatchers.IO])
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun loadMoreData(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        // check if is in progress state and if is then do not re-fetch again
        if(properties.progress.nonNullValue) return
        finishJobIfActive()
        properties.job = viewModelScope.launch(dispatcher) { fetchPokemonList() }
    }

    /**
     *
     * __TEST READY__
     *
     * Refresh list using [loadTypes] method
     * @param dispatcher the dispatcher to execute the coroutine task (default is [Dispatchers.IO])
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun refresh(dispatcher: CoroutineDispatcher = Dispatchers.IO) = loadTypes(dispatcher)

    /**
     *
     * __TEST READY__
     *
     * Method to trigger a refresh only if list is empty.
     *
     * __UseCase:__
     *
     * When user first open application, list has no items. User could open favorites list to preview
     * his or her favorite pokemon. When user navigate back from favorites, list will remain empty cause
     * fetching process has been canceled. To load pokemon, user has to click to another category to
     * start fetching process again. To avoid this, favorites fragment should send a trigger when
     * user go back and if list is empty then application should restart fetching pokemon from this
     * selected category.
     * @param dispatcher the dispatcher to execute the coroutine task (default is [Dispatchers.IO])
     * @author SotirisSapak
     * @since 1.0.0
     */
    fun refreshIfEmpty(dispatcher: CoroutineDispatcher = Dispatchers.IO) {
        // pokemon list may not be empty but list is not properly bind if pokemonList items is lower
        // than the offset of the pagination...
        // Example:
        // Stop process when pokemonList has 15 items
        // Offset = 20
        // List is not bind
        if(pokemonList.value?.count()!! < paginationProperties.offset) loadTypes(dispatcher)
    }


    companion object {

        const val TAG_FETCH_SELECTED_TYPE_DATA = "fetchSelectedTypeData"
        const val TAG_FETCH_POKEMON = "fetchPokemon"
        const val TAG_FETCH_MORE_POKEMON = "fetchMorePokemon"

        fun factory(host: HostViewModel) = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PokemonApplication)
                val typeService = application.getTypeService()
                val pokemonService = application.getPokemonService()
                HomeViewModel(host, typeService, pokemonService)
            }
        }

    }

}