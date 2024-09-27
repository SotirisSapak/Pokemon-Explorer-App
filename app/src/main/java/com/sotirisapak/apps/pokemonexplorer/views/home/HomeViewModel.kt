package com.sotirisapak.apps.pokemonexplorer.views.home

import android.util.Log
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sotirisapak.apps.pokemonexplorer.adapters.TypeAdapter
import com.sotirisapak.apps.pokemonexplorer.models.Type
import com.sotirisapak.apps.pokemonexplorer.views.host.HostViewModel
import com.sotirisapak.libs.pokemonexplorer.backend.models.Pokemon
import com.sotirisapak.libs.pokemonexplorer.backend.models.PokemonType
import com.sotirisapak.libs.pokemonexplorer.backend.remote.services.PokemonService
import com.sotirisapak.libs.pokemonexplorer.backend.remote.services.TypeService
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

    /**
     * Class to provide all adapters to [HomeViewModel] as a group
     * @author SotirisSapak
     * @since 1.0.0
     */
    inner class Adapters {
        val typeAdapter = TypeAdapter { _, selectedType -> fetchPokemonListByTypeSelection(selectedType) }
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

    private val listOfPokemonTypes = mutableListOf<PokemonType.TypePokemon>()
    private val paginatedListOfPokemon = mutableListOf<Pokemon>()

    val adapters = Adapters()
    val pagination = Pagination()

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
        fetchPokemonListByTypeSelection(arrayOfTypes[0])
    }

    /**
     * Fetch selected pokemon type based on adapter's selected item [Type].
     *
     * Why we need to fetch pokemon type?
     *
     * * In Api, fetching pokemon Type, the return object will include all related pokemon in a list.
     *  So fetching types will also fetch pokemon within this type. This method, will automatically
     *  parse all pokemon into list of [Pokemon] objects by calling [fetchPokemonListByType]
     *
     * @author SotirisSapak
     * @since 1.0.0
     */
    private fun fetchPokemonListByTypeSelection(type: Type) = newApiJob(
        tag = TAG_FETCH_TYPE_BY_ID,
        action = { typeService.getTypeById(type.id) },
        onError = { Log.e(TAG_FETCH_TYPE_BY_ID, it) }
    ) { fetchedItem ->
        // we need to check first if the return item has empty name or pokemon list...
        if(fetchedItem.name.isEmpty()) {
            Log.e(TAG_FETCH_TYPE_BY_ID, "Could not parse api model to type")
            return@newApiJob
        }
        if(fetchedItem.pokemonList.isEmpty()) {
            Log.e(TAG_FETCH_TYPE_BY_ID, "Cannot fetch the pokemon list...Is not valid")
            return@newApiJob
        }
        // we need to reset pagination index
        pagination.index = 0
        // clear viewModel list
        listOfPokemonTypes.clear()
        // assign the pokemon types to viewModel list
        listOfPokemonTypes.addAll(fetchedItem.pokemonList)
        Log.d(TAG_FETCH_TYPE_BY_ID, "Added ${listOfPokemonTypes.count()} pokemon to list of types")
        // fetch pokemon
        fetchPokemonWithPagination()
        fetchPokemonWithPagination()
        fetchPokemonWithPagination()
        fetchPokemonWithPagination()
        fetchPokemonWithPagination()
        fetchPokemonWithPagination()
        fetchPokemonWithPagination()
        fetchPokemonWithPagination()

    }


    private fun fetchPokemonWithPagination() {
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
        for(i in itemsToFetch) {
            Log.d(TAG_FETCH_POKEMON_BY_URL, i.toString())
        }
    }



    private fun fetchPokemonListByType(pokemonType: PokemonType) = newJob(
        tag = TAG_FETCH_POKEMON_BY_URL
    ) {
        Log.d(TAG_FETCH_POKEMON_BY_URL, "Fetching pokemon list...")
        val paginationList = fetchPokemonNext(pagination.offset, pokemonType.pokemonList)
        // add pagination list to actual list
        // listOfPokemonTypes.addAll(paginationList)
        Log.d(TAG_FETCH_POKEMON_BY_URL, "Fetch completed!")
    }


    private suspend fun fetchPokemonNext(
        numberOfItemsToFetch: Int,
        list: List<PokemonType.TypePokemon>
    ): List<Pokemon> {
        val returnList = mutableListOf<Pokemon>()
        for(i in pagination.index..<list.count()) {
            var count = 0
            when(val response = pokemonService.getPokemonByUrl(list[i].pokemonInformation.url)) {
                is ApiResult.Failure -> {}
                is ApiResult.Success -> {
                    returnList.add(response.data)
                }
            }
            count++
            if(count == numberOfItemsToFetch) break
        }
        return returnList
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