package com.sotirisapak.apps.pokemonexplorer.views.favorites

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.ViewGroup
import com.sotirisapak.apps.pokemonexplorer.databinding.FragmentFavoritesBinding
import com.sotirisapak.libs.pokemonexplorer.core.app.FragmentBase

class FavoritesFragment : FragmentBase<FragmentFavoritesBinding>() {

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

    }

    /**
     * Action implementation for back pressed callback.
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun onBack() {
        // at this point...back button should return to home fragment

    }

}