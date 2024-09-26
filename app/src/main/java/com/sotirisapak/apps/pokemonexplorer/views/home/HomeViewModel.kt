package com.sotirisapak.apps.pokemonexplorer.views.home

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sotirisapak.apps.pokemonexplorer.adapters.TypeAdapter
import com.sotirisapak.apps.pokemonexplorer.models.Type
import com.sotirisapak.apps.pokemonexplorer.views.host.HostViewModel
import com.sotirisapak.libs.pokemonexplorer.core.lifecycle.ViewModelBase

/**
 * ViewModel implementation for [HomeFragment]
 * @author SotirisSapak
 * @since 1.0.0
 */
class HomeViewModel(
    private val hostViewModel: HostViewModel
): ViewModelBase() {

    /**
     * Class to provide all adapters to [HomeViewModel] as a group
     * @author SotirisSapak
     * @since 1.0.0
     */
    inner class Adapters {

        val typeAdapter = TypeAdapter { bind, selectedType ->

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

    companion object {

        fun factory(host: HostViewModel) = viewModelFactory {
            initializer {
                HomeViewModel(host)
            }
        }

    }

}