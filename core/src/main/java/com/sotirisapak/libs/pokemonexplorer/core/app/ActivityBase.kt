package com.sotirisapak.libs.pokemonexplorer.core.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.viewbinding.ViewBinding

/**
 * A new way to implement your activities. This class will provide an abstract way to create a new
 * activity in order to use the lifecycle infrastructure by default. This class will also provide
 * a simple interface to interact with your [ViewBinding] properties as well. Last but not least, with
 * this class you can also use [ActivityBase.observe] method to create quick viewModel observers.
 * @param edgeToEdge edge to edge feature to be enabled or not.
 * @author SotirisSapak
 * @since 1.0.0
 */
abstract class ActivityBase<VB: ViewBinding>(private val edgeToEdge: Boolean = true): AppCompatActivity() {


    companion object {

        /**
         * Provide support for edge to edge feature for any [AppCompatActivity] class
         * @param activity the activity to enable edge to edge feature
         * @author SotirisSapak
         * @since 1.0.0
         */
        fun edgeToEdgeSupport(activity: AppCompatActivity) = activity.enableEdgeToEdge()

    }

    /**
     * The generated binding property as private instance cause will use a getter method
     *
     * @see [BaseActivity.binding]
     * @author SotirisSapak
     * @since 1.0.0
     */
    private var _binding: VB? = null

    /**
     * Binding property to use in order to configure [VB] attributes and views
     * @author SotirisSapak
     * @since 1.0.0
     */
    protected val binding get() = _binding!!

    /**
     * Override the default onCreate method in order to configure data binding and other properties
     * as well.
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        // enable feature transitions to this activity
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        // extend the transitions with this function...can override user to write custom activity
        // transitions
        activityTransition()
        // call super class method
        super.onCreate(savedInstanceState)
        // enable edge to edge feature for this activity
        if(edgeToEdge) enableEdgeToEdge()
        // inflate binding
        _binding = inflate(layoutInflater)
        // construct options
        attach()
        // apply root view to layout
        setContentView(binding.root)
        // continue custom activity configuration
        onCreation(savedInstanceState)
    }

    /**
     * Create custom transition animation for this activity. By default, there is no custom transitions
     * @author SotirisSapak
     * @since 1.0.0
     */
    open fun activityTransition() { /* no custom animation by default */ }

    /**
     * Clear the viewBinding property when the activity is destroyed
     * @author SotirisSapak
     * @since 1.0.0
     */
    protected fun clearBinding() {
        _binding = null
    }

    /**
     * Override the [BaseActivity.onDestroy] method in order to clear the [binding] property from this
     * fragment. If you want to re-override it to calling activity, should definitely use
     * [clearBinding] method.
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun onDestroy() {
        super.onDestroy()
        // remove the binding instance
        clearBinding()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //                                   Abstract methods
    ////////////////////////////////////////////////////////////////////////////////////////////////

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
    abstract fun inflate(inflater: LayoutInflater): VB

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
    abstract fun attach()

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
    abstract fun onCreation(savedInstanceState: Bundle?)

}

////////////////////////////////////////////////////////////////////////////////////////////////////
//                                        Extensions
////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Create a new observer for any [LiveData] property.
 * @param property the property to observe
 * @param action what to execute when [property] change value
 * @author SotirisSapak
 * @since 1.0.0
 */
inline fun <reified T: Any, VB: ViewBinding> ActivityBase<VB>.observe(
    property: LiveData<T>,
    crossinline action: (it: T) -> Unit)
{
    property.observe(this) { action.invoke(it) }
}