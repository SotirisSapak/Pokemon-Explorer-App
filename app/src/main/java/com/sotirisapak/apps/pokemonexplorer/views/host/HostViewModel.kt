package com.sotirisapak.apps.pokemonexplorer.views.host

import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.sotirisapak.libs.pokemonexplorer.core.lifecycle.ViewModelBase

/**
 * ViewModel implementation for [HostActivity] and nested fragments
 * @author SotirisSapak
 * @since 1.0.0
 */
class HostViewModel: ViewModelBase() {





    companion object {

        /**
         * Factory for [HostViewModel] lazy initialization in [HostActivity]
         */
        val factory = viewModelFactory {
            initializer {
                HostViewModel()
            }
        }
    }

}