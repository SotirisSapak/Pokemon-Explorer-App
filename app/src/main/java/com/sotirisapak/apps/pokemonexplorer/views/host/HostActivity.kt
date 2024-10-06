package com.sotirisapak.apps.pokemonexplorer.views.host

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.sotirisapak.apps.pokemonexplorer.databinding.ActivityHostBinding
import com.sotirisapak.libs.pokemonexplorer.core.app.ActivityBase

/**
 * Nothing more than a standard host activity to support navigation system with shared properties
 * within hostViewModel instance
 * @author SotirisSapak
 * @since 1.0.0
 */
class HostActivity : ActivityBase<ActivityHostBinding>() {

    /** The viewModel instance */
    private val hostViewModel: HostViewModel by viewModels { HostViewModel.factory }

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
    override fun inflate(inflater: LayoutInflater) = ActivityHostBinding.inflate(inflater)

    /**
     * Attach any binding properties. This method will be called before
     * <code>
     *
     *     setContentView()
     * </code>
     *
     * method.
     *
     * Usage example:
     *
     * <code>
     *
     *     override fun attach() {
     *          binding.lifecycleOwner = this
     *          // attach any xml binding properties
     *          binding.viewModel = myViewModel
     *          // etc ..
     *     }
     * </code>
     *
     * To configure the activity operations, use [onCreation] abstract method
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun attach() {
        binding.lifecycleOwner = this
        binding.viewModel = hostViewModel
    }

    /**
     * Configure the activity logic. Will be called in [onCreate] method after:
     *
     * <code>
     *
     *     setContentView()
     * </code>
     *
     * method.
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun onCreation(savedInstanceState: Bundle?) { /* nothing to attach */ }

}