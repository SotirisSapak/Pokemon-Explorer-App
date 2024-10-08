package com.sotirisapak.apps.pokemonexplorer.views.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sotirisapak.apps.pokemonexplorer.R
import com.sotirisapak.apps.pokemonexplorer.adapters.PokemonAdapter
import com.sotirisapak.apps.pokemonexplorer.adapters.TypeAdapter
import com.sotirisapak.apps.pokemonexplorer.databinding.FragmentHomeBinding
import com.sotirisapak.apps.pokemonexplorer.views.host.HostActivity
import com.sotirisapak.apps.pokemonexplorer.views.host.HostViewModel
import com.sotirisapak.libs.pokemonexplorer.core.app.FragmentBase
import com.sotirisapak.libs.pokemonexplorer.core.app.observe
import com.sotirisapak.libs.pokemonexplorer.core.app.observeNavigationResult
import com.sotirisapak.libs.pokemonexplorer.core.extensions.clear
import com.sotirisapak.libs.pokemonexplorer.framework.bottomRoundedInsets
import com.sotirisapak.libs.pokemonexplorer.framework.topRoundedInsets

/**
 * At home, user will have the option to navigate through all types and items. Types will be referenced
 * as filters and items will be fetched based on this specific filter id.
 * @author SotirisSapak
 * @since 1.0.0
 */
class HomeFragment : FragmentBase<FragmentHomeBinding>() {

    // ? ==========================================================================================
    // ? ViewModels
    // ? ==========================================================================================

    /**
     * Cause [HomeFragment] is a nested fragment into [HostActivity], we are able to bind the host
     * viewmodel into any nested fragment
     */
    private val hostViewModel: HostViewModel by activityViewModels()

    /** ViewModel instance of this fragment */
    private val viewModel: HomeViewModel by viewModels { HomeViewModel.factory(hostViewModel) }

    // ? ==========================================================================================
    // ? Adapters
    // ? ==========================================================================================

    /**
     * Adapter to bind the recyclerView related to pokemon types
     */
    private val typeAdapter = TypeAdapter { _, selectedType -> viewModel.onTypeClick(selectedType = selectedType) }

    /**
     * Adapter to bind the recyclerView related to pokemon
     */
    private val pokemonAdapter = PokemonAdapter(onPokemonClick = { _, clickedPokemon ->
        viewModel.onPokemonClick(clickedPokemon)
    })

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
    override fun inflate(inflater: LayoutInflater) = FragmentHomeBinding.inflate(inflater)

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
        // ? ------------ Utilities ------------
        attachLayoutInsets()
        attachAdaptersToRecyclerViews()
        // ? ------------ Observers ------------
        observeAdapters()
        observerForProceed()
        observerForOnFavoritesBack()
        // ? ------------ Listeners ------------
        binding.nestedScrollViewContent.setOnScrollChangeListener(onScrollListener)
        binding.buttonErrorRefresh.setOnClickListener(onErrorRefreshListener)
        binding.buttonFavorites.setOnClickListener(onFavoriteClick)
    }

    /**
     * Action implementation for back pressed callback.
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun onBack() {
        // at this stage, if user click back then should close the application
        baseActivity.finishAffinity()
    }


    // ? ==========================================================================================
    // ? Utilities
    // ? ==========================================================================================
    private fun attachLayoutInsets() {
        binding.layoutHeader.topRoundedInsets()
        binding.layoutContainerLinear.bottomRoundedInsets()
    }
    private fun attachAdaptersToRecyclerViews() {
        binding.recyclerViewTypes.adapter = typeAdapter
        binding.recyclerViewPokemon.adapter = pokemonAdapter
    }


    // ? ==========================================================================================
    // ? Observers
    // ? ==========================================================================================
    private fun observerForProceed() = observe(viewModel.properties.proceed) {
        if(it) {
            // in order to avoid unwanted results and save some network traffic...remove any pending
            // job from stack
            // viewModel.finishAllJobs()
            // clear the property
            viewModel.properties.proceed.clear()
            findNavController().navigate(R.id.actionHomeToPreview)
        }
    }
    private fun observerForOnFavoritesBack() = observeNavigationResult<Boolean>("onFavoritesBack") {
        Log.d("observerForOnFavoritesBack", "Value of \"onFavoritesBack\": $it")
        if(it) viewModel.refreshIfEmpty()
    }
    private fun observeAdapters() {
        observe(viewModel.types) { items -> typeAdapter.submitList(items) }
        observe(viewModel.pokemonList) { items -> pokemonAdapter.submitList(items) }
    }
    // ? ==========================================================================================
    // ? Listeners
    // ? ==========================================================================================
    private val onScrollListener = NestedScrollView.OnScrollChangeListener {
        v: NestedScrollView, _: Int, scrollY: Int, _: Int, _: Int ->
        // on scroll change we are checking when users scroll at bottom.
        if (scrollY == v.getChildAt(0).measuredHeight - v.measuredHeight) {
            viewModel.loadMoreData()
        }
    }
    private val onErrorRefreshListener = View.OnClickListener { viewModel.refresh() }
    private val onFavoriteClick = View.OnClickListener {
        // in order to avoid unwanted results and save some network traffic...remove any pending
        // job from stack
        viewModel.finishJobIfActive()
        findNavController().navigate(R.id.action_homeToFavorites)
    }

}