package com.sotirisapak.apps.pokemonexplorer.views.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sotirisapak.apps.pokemonexplorer.databinding.FragmentPreviewBinding
import com.sotirisapak.apps.pokemonexplorer.views.host.HostViewModel
import com.sotirisapak.libs.pokemonexplorer.core.app.FragmentBase
import com.sotirisapak.libs.pokemonexplorer.core.app.observe
import com.sotirisapak.libs.pokemonexplorer.core.app.setNavigationResult
import com.sotirisapak.libs.pokemonexplorer.framework.bottomRoundedInsets
import com.sotirisapak.libs.pokemonexplorer.framework.topRoundedInsets
import com.squareup.picasso.Picasso

/**
 * Fragment implementation to preview a selected pokemon attributes along with the list item information
 * @author SotirisSapak
 * @since 1.0.0
 */
class PreviewFragment : FragmentBase<FragmentPreviewBinding>() {

    /** ViewModel instance from host activity to bind shared properties */
    private val hostViewModel: HostViewModel by activityViewModels()

    /** ViewModel instance for this fragment */
    private val viewModel: PreviewViewModel by viewModels { PreviewViewModel.factory(hostViewModel) }

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
    override fun inflate(inflater: LayoutInflater) = FragmentPreviewBinding.inflate(inflater)

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
    override fun onCreation() {}

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
        provideImageToHeaderViaUrl()
        provideStats()
        // ? ------------ Observers ------------
        observerForIsFavorite()
        // ? ------------ Listeners ------------
        binding.buttonFavorite.setOnClickListener(onFavoriteClick)
        binding.buttonBack.setOnClickListener(onBackClick)
    }

    /**
     * Action implementation for back pressed callback.
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun onBack() {
        viewModel.onBackPressed {
            // we should send a signal back to calling fragment
            // This signal will only be read by FavoriteFragment cause there is a case that the user
            // will preview the favorite list and uncheck this pokemon. So, favorite fragment need
            // to refresh its list.
            setNavigationResult("requireUpdate", viewModel.returnRefreshListSignal)
            findNavController().popBackStack()
        }
    }


    // ? ==========================================================================================
    // ? Utilities
    // ? ==========================================================================================
    private fun attachLayoutInsets(){
        binding.layoutHeader.topRoundedInsets()
        binding.layoutStats.bottomRoundedInsets()
    }
    private fun provideImageToHeaderViaUrl() {
        Picasso
            .get()
            .load(viewModel.selectedPokemon.sprites.frontDefault)
            .into(binding.imagePokemonIcon)
    }
    private fun provideStats() {

    }


    // ? ==========================================================================================
    // ? Observers
    // ? ==========================================================================================
    private fun observerForIsFavorite() = observe(viewModel.isFavorite) {
        binding.buttonFavorite.isSelected = it
    }


    // ? ==========================================================================================
    // ? Listeners
    // ? ==========================================================================================
    private val onFavoriteClick = View.OnClickListener { viewModel.onFavoriteSelection() }
    private val onBackClick = View.OnClickListener { baseActivity.onBackPressedDispatcher.onBackPressed() }

}