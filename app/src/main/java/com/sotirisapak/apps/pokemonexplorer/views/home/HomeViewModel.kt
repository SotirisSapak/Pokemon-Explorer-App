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

    private val listOfPokemonTypes = mutableListOf<PokemonType.TypePokemon>()
    private val paginatedListOfPokemon = mutableListOf<Pokemon>()
    private var previousSelectedType = Type()

    /**
     * Class to provide all adapters to [HomeViewModel] as a group
     * @author SotirisSapak
     * @since 1.0.0
     */
    inner class Adapters {
        val typeAdapter = TypeAdapter { _, selectedType ->
            // to avoid re-creating adapter when the already selected list is clicked again,
            // we should make program remember if the selected type is the one which is clicked
            if(previousSelectedType != selectedType) {
                fetchDataByType(selectedType)
            }
        }
        val pokemonAdapter = PokemonAdapter { _, clickedPokemon ->
            hostViewModel.selectedPokemon = clickedPokemon
            // trigger a signal to view to go to preview fragment
            properties.proceed.set()
        }

    }

    /**
     * Class to provide pagination attributes
     * @author SotirisSapak
     * @since 0.0.1
     */
    inner class Pagination {

        /**
         * Offset is the amount of items to fetch next. Default is 20
         */
        var offset = 20

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

    val adapters = Adapters()
    private val pagination = Pagination()

    init {
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
        // remove any previous type in list if available
        adapters.typeAdapter.submitList(emptyList())
        adapters.typeAdapter.submitList(arrayOfTypes)
        // on initialization...we should automatically fetch the data for the first selected type (id = 9)
        fetchDataByType(arrayOfTypes[0])
        previousSelectedType = arrayOfTypes[0]
    }

    /**
     * Fetch selected pokemon type based on adapter's selected item [Type].
     *
     * Why we need to fetch pokemon type?
     *
     * * In Api, fetching pokemon Type, the return object will include all related pokemon in a list.
     *  So fetching types will also fetch pokemon within this type. This method, will automatically
     *  parse all pokemon into list of [Pokemon] objects by calling [fetchDataByType]
     *
     * @author SotirisSapak
     * @since 1.0.0
     */
    private fun fetchDataByType(type: Type) = newJob {
        previousSelectedType = type
        // at this method, we should fetch first the type id to proper call pokemon fetch via type id
        // ? Properties initialization
        properties.error.clear()
        properties.progress.set()
        // ? ---------------------------
        // ? list and adapter reset
        paginatedListOfPokemon.clear()
        adapters.pokemonAdapter.submitList(emptyList())
        // ? ---------------------------
        // fetch the type ids
        var typeResponse: ApiResult<PokemonType, String> = ApiResult.onSuccess(PokemonType())
        onBackground { typeResponse = typeService.getTypeById(type.id) }
        // check the response data
        when(typeResponse){
            is ApiResult.Failure -> {
                val smartCastedResponse = typeResponse as ApiResult.Failure<PokemonType, String>
                // stop the process here, and preview the error information to user
                properties.progress.clear()
                properties.error.set(smartCastedResponse.error)
                Log.e(TAG_FETCH_TYPE_BY_ID, smartCastedResponse.error)
            }
            is ApiResult.Success -> {
                val smartCastedResponse = typeResponse as ApiResult.Success<PokemonType, String>
                // we need to reset pagination index
                pagination.index = 0
                // clear viewModel list
                listOfPokemonTypes.clear()
                // assign the pokemon types to viewModel list
                listOfPokemonTypes.addAll(smartCastedResponse.data.pokemonList)
                // now, handle pokemon pagination fetching using fetchPokemonWithPagination() method
                fetchPokemonWithPagination()
            }
        }

    }

    fun loadMoreData() = newJob {
        properties.progress.set()
        properties.error.clear()
        fetchPokemonWithPagination()
    }
    fun refresh() = fetchDataByType(previousSelectedType)

    private suspend fun fetchPokemonWithPagination() {
        Log.d(TAG_FETCH_POKEMON_BY_URL, "===========================")
        Log.d(TAG_FETCH_POKEMON_BY_URL, "Pagination index: ${pagination.index}")
        Log.d(TAG_FETCH_POKEMON_BY_URL, "Pagination offset: ${pagination.offset}")
        Log.d(TAG_FETCH_POKEMON_BY_URL, "===========================")
        // configure the list of urls that we have to fetch the data
        val itemsToFetch = mutableListOf<PokemonType.TypePokemon>()
        var count = 0
        for(i in pagination.index..<listOfPokemonTypes.count()) {
            itemsToFetch.add(listOfPokemonTypes[i])
            count++
            pagination.index++
            if(count == pagination.offset) {
                Log.d(TAG_FETCH_POKEMON_BY_URL,
                    "Pagination limit reached { " +
                            "Count: $count, " +
                            "Index: ${pagination.index}, " +
                            "Offset: ${pagination.offset} }")
                break
            }
        }
        Log.d(TAG_FETCH_POKEMON_BY_URL, "===========================")
        Log.d(TAG_FETCH_POKEMON_BY_URL, "(Post) Pagination index: ${pagination.index}")
        Log.d(TAG_FETCH_POKEMON_BY_URL, "(Post) Pagination offset: ${pagination.offset}")
        Log.d(TAG_FETCH_POKEMON_BY_URL, "===========================")

        for(pokemonInfo in itemsToFetch){
            var response: ApiResult<Pokemon, String> = ApiResult.onSuccess(Pokemon())
            onBackground { response = pokemonService.getPokemonByUrl(pokemonInfo.pokemonInformation.url) }
            // check the response data
            when(response) {
                is ApiResult.Failure -> { /* ignore this pokemon */ }
                is ApiResult.Success -> {
                    val smartCastedResponse = response as ApiResult.Success<Pokemon, String>
                    // add this pokemon to viewModel list
                    paginatedListOfPokemon.add(smartCastedResponse.data)
                }
            }
        }

        // check if data is empty or not
        if(paginatedListOfPokemon.isEmpty()) {
            Log.e(TAG_FETCH_POKEMON_BY_URL, "Pokemon list is empty")
            return
        }
        // set the result to adapter
        adapters.pokemonAdapter.submitList(paginatedListOfPokemon)
        // clear progress
        properties.progress.clear()
    }



    companion object {

        const val TAG_FETCH_TYPE_BY_ID = "tag_fetchTypeById"
        const val TAG_FETCH_POKEMON_BY_URL = "tag_fetchPokemonByUrl"

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