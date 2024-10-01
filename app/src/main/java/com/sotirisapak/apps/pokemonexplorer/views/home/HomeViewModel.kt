package com.sotirisapak.apps.pokemonexplorer.views.home

import android.util.Log
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sotirisapak.apps.pokemonexplorer.adapters.PokemonAdapter
import com.sotirisapak.apps.pokemonexplorer.adapters.TypeAdapter
import com.sotirisapak.apps.pokemonexplorer.models.Type
import com.sotirisapak.apps.pokemonexplorer.views.host.HostViewModel
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon
import com.sotirisapak.libs.pokemonexplorer.backend.models.PokemonType
import com.sotirisapak.libs.pokemonexplorer.backend.remote.services.PokemonService
import com.sotirisapak.libs.pokemonexplorer.backend.remote.services.TypeService
import com.sotirisapak.libs.pokemonexplorer.core.extensions.clear
import com.sotirisapak.libs.pokemonexplorer.core.extensions.onBackground
import com.sotirisapak.libs.pokemonexplorer.core.extensions.set
import com.sotirisapak.libs.pokemonexplorer.core.lifecycle.ViewModelBase
import com.sotirisapak.libs.pokemonexplorer.core.models.ApiResult
import com.sotirisapak.libs.pokemonexplorer.di.components.PokemonApplication
import androidx.lifecycle.viewModelScope
import com.sotirisapak.libs.pokemonexplorer.core.extensions.onUiThread

/**
 * ViewModel implementation for [HomeFragment]
 * @author SotirisSapak
 * @since 1.0.0
 */
class HomeViewModel(
    private val hostViewModel: HostViewModel,
    private val typeService: TypeService,
    private val pokemonService: PokemonService
): ViewModelBase() {

    // ? ==========================================================================================
    // ? Nested classes for better organization
    // ? ==========================================================================================
    /**
     * Class to provide all adapters to [HomeViewModel] as a group
     * @author SotirisSapak
     * @since 1.0.0
     */
    inner class Adapters {
        val typeAdapter = TypeAdapter { _, selectedType ->
            // to avoid re-creating adapter when the already selected list is clicked again,
            // we should make program remember if the selected type is the one which is clicked
            if(selectedPokemonType != selectedType) {
                selectedPokemonType = selectedType
                loadSelectedTypeData()
            }
        }
        val pokemonAdapter = PokemonAdapter { _, clickedPokemon ->
            hostViewModel.selectedPokemon = clickedPokemon
            finishAllJobs()
            // trigger a signal to view to go to preview fragment
            properties.proceed.set()
        }
    }

    /**
     * Class to provide pagination attributes
     * @author SotirisSapak
     * @since 1.0.0
     */
    inner class Pagination {

        /**
         * Offset is the amount of items to fetch next. Default is 10
         */
        var offset = 10

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
     * The pokemon list as standard information in a private list. This list, will be used to hold all the pokemon
     * fetched from the selected type, before any pagination applied and actual pokemon fetching via
     * [PokemonType.TypePokemon.PokemonInformation.url]
     */
    private val pokemonTypes = mutableListOf<PokemonType.TypePokemon>()

    /**
     * The list of pokemon to hold all the fetched data from api. This list will hold all pokemon
     * and with the use of pagination technique, will save only a small junk of data
     */
    private val pokemonList = mutableListOf<Pokemon>()

    /**
     * Variable to hold a state for the type adapter to prevent reload current selected type its data.
     * @see TypeAdapter
     */
    private var selectedPokemonType = Type()

    // ? ------------------------
    // ? Class instances
    // ? ------------------------

    /**
     * Instance of [Adapters] class
     */
    val adapters = Adapters()

    /**
     * Instance of [Pagination] class
     */
    private val paginationProperties = Pagination()

    // ? ==========================================================================================
    // ? Ui thread methods
    // ? ==========================================================================================

    init { onInit() }

    /**
     * We need to set firstly, the 10 most famous categories of pokemon. These categories will
     * be saved as [Type] which is a standard model for pokemon categories before fetching its
     * data which will be saved as [PokemonType] objects.
     * @author SotirisSapak
     * @since 1.0.0
     */
    private fun onInit() {
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
        // clear adapters just in case!
        adapters.typeAdapter.submitList(emptyList())
        adapters.pokemonAdapter.submitList(emptyList())

        // attach the standard types to typeAdapter to show as options within the recyclerView via
        // dataBinding
        adapters.typeAdapter.submitList(arrayOfTypes)
        // Now, we have to fetch the data for the selected type which is the "Steel".
        selectedPokemonType = arrayOfTypes[0]
        // fetch all data for "Steel"
        loadSelectedTypeData()
    }

    // ? ==========================================================================================
    // ? Background tasks
    // ? ==========================================================================================

    /**
     * __TAG__ -> [TAG_FETCH_SELECTED_TYPE_DATA]
     *
     * __COMPLEXITY__ -> Medium due to extended log output
     *
     * Method to fetch the data of the selected type. No need to pass a parameter cause the [selectedPokemonType]
     * will be used to fetch its data.
     *
     * Hint: This method will use the [newJob] method to generate a new coroutine job within [viewModelScope].
     * @author SotirisSapak
     * @since 1.0.0
     */
    private fun loadSelectedTypeData() = newJob(
        tag = TAG_FETCH_SELECTED_TYPE_DATA,
        notifyException = false
    ) { tag ->

        /*
            This process will perform the task for fetching the selected type information. When any
            type is selected, then we have to remove any pokemon left in pokemonList and reset the
            pokemon adapter accordingly
        */

        // we must break terminate all jobs in order to avoid unwanted list replacements
        finishAllJobsExceptTag(tag)
        // reset pagination index
        paginationProperties.index = 0
        // set process to true and error to false to avoid ui overlay
        properties.progress.set()
        properties.error.clear()
        // reset pokemon list and adapter cause we have selected a new type
        pokemonList.clear()
        adapters.pokemonAdapter.submitList(pokemonList)
        // we need to fetch the type information from api. The result will be saved in a response
        // variable
        var response: ApiResult<PokemonType, String> = ApiResult.onFailure("")
        // do this action in background to avoid ui freeze!
        onBackground {
            response = typeService.getTypeById(selectedPokemonType.id)
        }
        // we must check the response data
        when(response) {
            is ApiResult.Failure -> {
                val smartCastedResponse = response as ApiResult.Failure<PokemonType, String>
                // when response return a Failure signal, then we have to preview the error and
                // stop the task. No need to do this cause newJob method will do this for us automatically
                properties.progress.clear()
                properties.error.set(smartCastedResponse.error)
                Log.e(tag, smartCastedResponse.error)
            }
            is ApiResult.Success -> {
                val smartCastedResponse = response as ApiResult.Success<PokemonType, String>
                // add fetched types to viewModel list if is not empty
                if(smartCastedResponse.data.pokemonList.isEmpty()) {
                    // Ahhh, for some reason, the list of pokemon is empty...unknown reason
                    properties.progress.clear()
                    properties.error.set("Unknown reason for not fetching pokemon for \"${smartCastedResponse.data.name}\"")
                    Log.e(tag, "Unknown reason for not fetching pokemon for \"${smartCastedResponse.data.name}\"")
                } else {
                    // add pokemon list to types after removing all previous items
                    pokemonTypes.clear()
                    pokemonTypes.addAll(smartCastedResponse.data.pokemonList)
                    Log.d(tag, "---------------------------------------------------------------")
                    Log.d(tag, "Type: ${selectedPokemonType.name}")
                    Log.d(tag, "Items fetched: ${pokemonTypes.size}")
                    Log.d(tag, "---------------------------------------------------------------")
                    for(i in pokemonTypes.indices) {
                        Log.d(tag, "\"${pokemonTypes[i].pokemonInformation.name}\" -> ${pokemonTypes[i].pokemonInformation.url}")
                        if(i == 5) {
                            Log.d(tag, " ... ")
                            Log.d(tag, " ... ")
                            Log.d(tag, " ** And other ${pokemonTypes.size - 5} ** ")
                            break
                        }
                    }
                    Log.d(tag, "---------------------------------------------------------------")
                    // now, handle pokemon pagination fetching using fetchPokemon() method
                    fetchPokemon()
                }
            }
        }
    }

    /**
     * __TAG__ -> [TAG_FETCH_POKEMON]
     *
     * __COMPLEXITY -> Low
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
    private suspend fun fetchPokemon(tag: String = TAG_FETCH_POKEMON) {
        // set process to true and error to false to avoid ui overlay
        properties.progress.set()
        properties.error.clear()
        // first we have to configure pagination list
        val paginatedList = configurePagination(tag, pokemonTypes)
        // using a for loop...fetch all pokemon information based on their url
        onBackground {
            for(pokemonInformation in paginatedList) {
                // fetch result to a response object
                var response: ApiResult<Pokemon, String> = ApiResult.onFailure("")
                response = pokemonService.getPokemonByUrl(pokemonInformation.pokemonInformation.url)
                // check the response data
                when(response) {
                    is ApiResult.Failure -> onUiThread {
                        // Ignore this pokemon
                        Log.i(tag,
                            "Pokemon with name \"${pokemonInformation.pokemonInformation.name}\" " +
                                    "is ignored due to error ${response.error}")
                    }
                    is ApiResult.Success -> {
                        // add this pokemon to viewModel list
                        pokemonList.add(response.data)
                    }
                }
            }
        }
        // check if data is empty or not
        if(pokemonList.isEmpty()) {
            properties.error.set("Cannot find pokemon on ${selectedPokemonType.name} type")
            Log.e(tag, "Cannot find pokemon on ${selectedPokemonType.name} type")
        } else {
            // set the result to adapter
            adapters.pokemonAdapter.submitList(pokemonList)
        }
        // clear progress
        properties.progress.clear()
    }


    /**
     * Task to configure pagination indexes and list items to fetch only those
     * @param tag the tag name to preview in log file. Default is [TAG_FETCH_POKEMON]
     * @param listToPaginate the list of items to use in pagination.
     * @return the paginated list by [Pagination.index] and [Pagination.offset]
     * @author SotirisSapak
     * @since 1.0.0
     */
    private fun <T> configurePagination(tag: String = TAG_FETCH_POKEMON, listToPaginate: List<T>): List<T> {
        Log.d(tag, "---------------------------------------------------------------")
        Log.d(tag, "Configure pagination")
        Log.d(tag, "---------------------------------------------------------------")
        Log.d(tag, "Total items: ${listToPaginate.count()}")
        Log.d(tag, "Current index: ${paginationProperties.index}")
        Log.d(tag, "Add ${paginationProperties.index} more items")
        Log.d(tag, "---------------------------")
        Log.d(tag, "Begin pagination...")


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


        Log.d(tag, "Pagination completed")
        Log.d(tag, "---------------------------")
        Log.d(tag, "Results")
        Log.d(tag, "---------------------------")
        Log.d(tag, "Current index: ${paginationProperties.index}")
        Log.d(tag, "---------------------------")

        return resultList
    }


    fun loadMoreData() = newJob(
        tag = TAG_FETCH_POKEMON,
        notifyException = false
    ){
        fetchPokemon()
    }

    fun refresh() = loadSelectedTypeData()


    companion object {

        const val TAG_FETCH_SELECTED_TYPE_DATA = "fetchSelectedTypeData"
        const val TAG_FETCH_POKEMON = "fetchPokemon"

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