package com.sotirisapak.apps.pokemonexplorer.views.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sotirisapak.apps.pokemonexplorer.R
import com.sotirisapak.apps.pokemonexplorer.databinding.FragmentFavoritesBinding
import com.sotirisapak.apps.pokemonexplorer.views.host.HostViewModel
import com.sotirisapak.libs.pokemonexplorer.core.app.FragmentBase
import com.sotirisapak.libs.pokemonexplorer.core.app.observe
import com.sotirisapak.libs.pokemonexplorer.core.app.observeNavigationResult
import com.sotirisapak.libs.pokemonexplorer.core.extensions.clear
import com.sotirisapak.libs.pokemonexplorer.framework.bottomRoundedInsets
import com.sotirisapak.libs.pokemonexplorer.framework.topRoundedInsets

/**
 * Framework to provide an interface to user to manipulate their favorite pokemon offline. This object
 * will be saved into local database via room and fetched within a standard dao interface
 * @author SotirisSapak
 * @since 1.0.0
 */
class FavoritesFragment : FragmentBase<FragmentFavoritesBinding>() {

    /** Host viewModel instance */
    private val hostViewModel: HostViewModel by activityViewModels()

    /** ViewModel instance */
    private val viewModel: FavoritesViewModel by viewModels { FavoritesViewModel.factory(hostViewModel) }

    /**
     * You have to inflate your dataBinding class as below:
     *
     * <code>
     *
     *     // VB is your viewBinding class
     *     override fun inflate(inflater: LayoutInflater) = VB.inflate(inflater)
     * </code>
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun inflate(inflater: LayoutInflater) = FragmentFavoritesBinding.inflate(inflater)

    /**
     * Attach any binding properties. This method will be called before
     * <code>
     *
     *     setContentView(binding.root)
     * </code>
     *
     * method.
     *
     * Usage example:
     *
     * <code>
     *
     *     override fun attach() {
     *          binding.lifecycleOwner = viewLifecycleOwner
     *          // attach any xml binding properties
     *          binding.viewModel = myViewModel
     *          // etc ..
     *     }
     * </code>
     *
     * To configure the fragment operations, use [onViewCreation] abstract method
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun attach() {
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
    }

    /**
     * Configure the fragment logic before creating the view. Will be called in [onCreate] method.
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun onCreation() { /* nothing to attach */ }

    /**
     * Override the [Fragment.onCreateView] method to generate our own method. Will be called after
     * <code>
     *
     *     setContentView(binding.root)
     * </code>
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun onViewCreation(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) {
        // ? ------------------ Utilities ------------------
        attachLayoutInsets()
        // ? ------------------ Utilities ------------------
        observeProceed()
        observeNavigationResultFromPreview()
        // ? ------------------ Listeners ------------------
        binding.buttonBack.setOnClickListener(onBackClick)
    }

    /**
     * Action implementation for back pressed callback.
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun onBack() {
        // at this point...back button should return to home fragment
        findNavController().popBackStack()
    }


    // ? ==========================================================================================
    // ? Utilities
    // ? ==========================================================================================
    private fun attachLayoutInsets() {
        binding.layoutHeader.topRoundedInsets()
        binding.recyclerViewPokemon.bottomRoundedInsets()
    }


    // ? ==========================================================================================
    // ? Observers
    // ? ==========================================================================================
    private fun observeNavigationResultFromPreview() =
        observeNavigationResult<Boolean>("requireUpdate") { listRequireUpdate ->
        if(listRequireUpdate) { viewModel.refresh() }
    }
    private fun observeProceed() = observe(viewModel.properties.proceed) {
        if(it) {
            findNavController().navigate(R.id.actionFavouritesToPreview)
            viewModel.properties.proceed.clear()
        }
    }


    // ? ==========================================================================================
    // ? Listeners
    // ? ==========================================================================================
    private val onBackClick = View.OnClickListener {
        viewModel.onBackPressed { findNavController().popBackStack() }
    }


}