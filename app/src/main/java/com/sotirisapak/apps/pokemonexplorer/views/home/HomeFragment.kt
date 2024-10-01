package com.sotirisapak.apps.pokemonexplorer.views.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import android.view.animation.AnimationUtils
import com.sotirisapak.apps.pokemonexplorer.R
import com.sotirisapak.apps.pokemonexplorer.databinding.FragmentHomeBinding
import com.sotirisapak.apps.pokemonexplorer.views.host.HostViewModel
import com.sotirisapak.libs.pokemonexplorer.core.app.FragmentBase
import com.sotirisapak.libs.pokemonexplorer.core.app.observe
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

    /**
     * Cause [HomeFragment] is a nested fragment into [HostActivity], we are able to bind the host
     * viewmodel into any nested fragment
     */
    private val hostViewModel: HostViewModel by activityViewModels()

    /** ViewModel instance of this fragment */
    private val viewModel: HomeViewModel by viewModels { HomeViewModel.factory(hostViewModel) }

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
        // ? ------------ Observers ------------
        observerForProceed()
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


    // ? ==========================================================================================
    // ? Observers
    // ? ==========================================================================================
    private fun observerForProceed() = observe(viewModel.properties.proceed) {
        if(it) {
            // in order to avoid unwanted results and save some network traffic...remove any pending
            // job from stack
            viewModel.finishAllJobs()
            findNavController().navigate(R.id.actionHomeToPreview)
            // clear the property
            viewModel.properties.proceed.clear()
        }
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
        viewModel.finishAllJobs()
        findNavController().navigate(R.id.action_homeToFavorites)
    }

}