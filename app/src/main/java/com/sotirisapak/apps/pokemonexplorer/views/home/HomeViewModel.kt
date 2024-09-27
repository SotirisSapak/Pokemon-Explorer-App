package com.sotirisapak.apps.pokemonexplorer.views.home

import android.util.Log
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sotirisapak.apps.pokemonexplorer.adapters.TypeAdapter
import com.sotirisapak.apps.pokemonexplorer.models.Type
import com.sotirisapak.apps.pokemonexplorer.views.host.HostViewModel
import com.sotirisapak.libs.pokemonexplorer.backend.remote.TypeRepository
import com.sotirisapak.libs.pokemonexplorer.core.lifecycle.ViewModelBase
import com.sotirisapak.libs.pokemonexplorer.di.components.PokemonApplication

/**
 * ViewModel implementation for [HomeFragment]
 * @author SotirisSapak
 * @since 1.0.0
 */
class HomeViewModel(
    private val hostViewModel: HostViewModel,
    private val typeRepository: TypeRepository
): ViewModelBase() {

    /**
     * Class to provide all adapters to [HomeViewModel] as a group
     * @author SotirisSapak
     * @since 1.0.0
     */
    inner class Adapters {

        val typeAdapter = TypeAdapter { bind, selectedType ->
            getApi(selectedType)
        }


    }

    val adapters = Adapters()

    init {
        val arrayOfTypes = mutableListOf(
            Type(9, "Steel", true),
            Type(10, "Fire"),
            Type(11, "Water"),
            Type(12, "Grass"),
            Type(13, "Electric"),
            Type(16, "Dragon"),
            Type(14, "Psychic"),
            Type(17, "Dark"),
            Type(18, "Fairy")
        )
        adapters.typeAdapter.submitList(arrayOfTypes)
    }


    private fun getApi(selectedType: Type) = newBackgroundJob {
        val response = typeRepository.getTypeById(selectedType.id)
        Log.d("getApi", response.toString())
        if(response.isSuccessful) {
            Log.d("getApi", response.body().toString())
            getPokemon(response.body()!!.pokemon[0].pokemon.url)
        } else {
            Log.e("getApi", response.errorBody().toString())
        }
    }

    private fun getPokemon(url: String) = newBackgroundJob {
        val response = typeRepository.getPokemonByUrl(url)
        Log.d("getPokemon", response.toString())
    }

    companion object {

        fun factory(host: HostViewModel) = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PokemonApplication)
                val repository = application.getTypeRepository()
                HomeViewModel(host, repository)
            }
        }

    }

}