package com.sotirisapak.libs.pokemonexplorer.core.app

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.google.android.material.transition.MaterialArcMotion
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialSharedAxis

/**
 * Fragment transition animation duration __(SLOW)__
 * @author SotirisSapak
 * @since 0.0.3
 */
const val FRAGMENT_ANIMATION_DURATION_SLOW = 800L

/**
 * Fragment transition animation duration __(DEFAULT)__
 * @author SotirisSapak
 * @since 0.0.3
 */
const val FRAGMENT_ANIMATION_DURATION_FAST = 250L

/**
 * Fragment transition animation duration __(FAST)__
 * @author SotirisSapak
 * @since 0.0.3
 */
const val FRAGMENT_ANIMATION_DURATION_SNAP = 150L

/**
 * A new way to implement your fragments. This class will provide an easy way to create a fragment
 * in order to safely use the lifecycle features. This class will also provide a simple interface
 * to interact with your [ViewBinding] properties as well. Last but not least, this class you can
 * also use [FragmentBase.observe] method to create quick viewModel observers.
 * @author SotirisSapak
 * @since 1.0.0
 */
abstract class FragmentBase<VB: ViewBinding>(
    private val transitionAxis: Int = MaterialSharedAxis.Z,
    private val transitionDuration: Long = FRAGMENT_ANIMATION_DURATION_FAST
): Fragment() {

    /**
     * The generated binding property as private instance cause will use a getter method
     *
     * @see [FragmentBase.binding]
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
     * Non-null activity property
     * @author SotirisSapak
     * @since 0.0.2
     */
    val baseActivity by lazy { requireActivity() }

    /**
     * Override the [Fragment.onCreate] method to generate our own method
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // attach back pressed callback
        requireActivity().onBackPressedDispatcher.addCallback(onBackPressedCallback)
        // attach transitions
        attachTransitions()
        // inflate the abstract method
        onCreation()
    }

    /**
     * Override the [Fragment.onCreateView] method to generate our own method
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // attach binding to variable
        _binding = inflate(inflater)
        // attach binding properties
        attach()
        // attach onCreateView abstract method
        onViewCreation(inflater, container, savedInstanceState)
        // return root view
        return binding.root
    }

    /**
     * Override the custom fragment static transitions with generic [MaterialSharedAxis] transition.
     * Can be override if you want to change completely the animation without using the [MaterialSharedAxis]
     * configuration.
     * @author SotirisSapak
     * @since 1.0.0
     */
    open fun attachTransitions() {
        enterTransition = MaterialSharedAxis(transitionAxis, true).apply {
            interpolator = LinearOutSlowInInterpolator()
            duration = transitionDuration
        }
        exitTransition = MaterialSharedAxis(transitionAxis, false).apply {
            interpolator = LinearOutSlowInInterpolator()
            duration = transitionDuration
        }
        returnTransition = MaterialSharedAxis(transitionAxis, false).apply {
            interpolator = LinearOutSlowInInterpolator()
            duration = transitionDuration
        }
        reenterTransition = MaterialSharedAxis(transitionAxis, true).apply {
            interpolator = LinearOutSlowInInterpolator()
            duration = transitionDuration
        }
    }

    /**
     * Clear the viewBinding property when the fragment is destroyed
     * @author SotirisSapak
     * @since 1.0.0
     */
    protected fun clearBinding() {
        _binding = null
    }

    /**
     * Override the [Fragment.onDestroy] method in order to clear the [binding] property from this
     * fragment. If you want to re-override it to calling fragment, should definitely use
     * [clearBinding] method.
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun onDestroy() {
        super.onDestroy()
        // remove the binding instance
        clearBinding()
    }

    /**
     * Override the [Fragment.onDestroyView] method in order to destroy the back pressed callback
     * @author SotirisSapak
     * @since 1.0.0
     */
    override fun onDestroyView() {
        super.onDestroyView()
        // destroy backPressed callback for this fragment and remove it from lifecycle
        onBackPressedCallback.isEnabled = false
        onBackPressedCallback.remove()
    }

    /**
     * Callback implementation for back pressed configuration
     * @author SotirisSapak
     * @since 1.0.0
     */
    private val onBackPressedCallback = object : OnBackPressedCallback(true){
        override fun handleOnBackPressed() = onBack()
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
    abstract fun attach()

    /**
     * Configure the fragment logic before creating the view. Will be called in [onCreate] method.
     * @author SotirisSapak
     * @since 1.0.0
     */
    abstract fun onCreation()

    /**
     * Override the [Fragment.onCreateView] method to generate our own method. Will be called after
     * <code>
     *
     *     setContentView(binding.root)
     * </code>
     * @author SotirisSapak
     * @since 1.0.0
     */
    abstract fun onViewCreation(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    )

    /**
     * Action implementation for back pressed callback.
     * @author SotirisSapak
     * @since 1.0.0
     */
    abstract fun onBack()
}


// ? ///////////////////////////////////////////////////////////////////////////////////////////////
// ?                                        Extensions
// ? ///////////////////////////////////////////////////////////////////////////////////////////////


/**
 * Create a new observer for any [LiveData] property.
 * @param property the property to observe
 * @param action what to execute when [property] change value
 * @author SotirisSapak
 * @since 1.0.0
 */
inline fun <reified T: Any, VB: ViewBinding> FragmentBase<VB>.observe(
    property: LiveData<T>,
    crossinline action: (it: T) -> Unit)
{
    property.observe(viewLifecycleOwner) { action.invoke(it) }
}

/**
 * Get result from navigation component fragment. This method will generate a new liveData value
 * that must be observed. To generate a new observer by default, use [observeNavigationResult] instead.
 * @see observeNavigationResult
 * @param key the key string value to get from the result fragment
 * @author SotirisSapak
 * @since 1.0.0
 */
fun <T> Fragment.getNavigationResultLiveData(key: String = "result") =
    findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<T>(key)

/**
 * Add a new result to navigation component fragment. This method will generate a new value
 * as a fragment result in order to fetch it from a previous fragment.
 *
 * Example:
 *
 * <code>
 *
 *     // FragmentA.kt (Receive fragment)
 *     ..
 *
 *     private fun getResult() = observeNavigationResult("resultA") { value ->
 *          Log.d("result", "Fragment B result: $value")
 *     }
 *
 *
 *     // FragmentB.kt (Send fragment)
 *     ..
 *
 *     private fun onBackImplementation() {
 *          // do something
 *          setNavigationResult("result", true)
 *          // simulate on back pressed action
 *          requireActivity().onBackPressedDispatcher.onBackPressed()
 *     }
 *
 * </code>
 *
 * From the above code the navigation is structured as:
 *
 * <code>
 *
 *     FragmentA -> FragmentB
 * </code>
 *
 * FragmentB will send a result value (true) to FragmentA and FragmentA will observe
 * the result and write a log line that show the return value if not null
 * @param key the key value to match the result indicator name
 * @param value the actual value to pass to result
 * @author SotirisSapak
 * @since 1.0.0 (last edited: 0.0.4)
 */
fun <T> Fragment.setNavigationResult(key: String = "result", value: T) {
    findNavController().previousBackStackEntry?.savedStateHandle?.set(key, value)
}

/**
 * Generate a new observer for the navigation result property. This trigger should be assigned as
 * method result in any view fragment as below:
 *
 * <code>
 *
 *     // FragmentCl.kt
 *     ..
 *     ..
 *     private fun getResult() = observeNavigationResult<Type>("result") { value ->
 *          // do something with return "value"
 *     }
 *     ..
 *     ..
 *
 * </code>
 * @author SotirisSapak
 * @since 0.0.4
 */
fun <T> Fragment.observeNavigationResult(
    key: String = "result",
    action: (T) -> Unit
) = getNavigationResultLiveData<T>(key)?.observe(viewLifecycleOwner) { action.invoke(it) }

/**
 * Use this [FragmentBase] extension to provide share element transition by default on [FragmentBase.onCreation]
 * method.
 * @param scrimThemeColor the scrim color to attach to transition. Can use background theme color
 * from ThemeSchema class on Framework module
 * @param durationMilli the long value for the animation duration
 * @param withArchMotion attach curve path is property set to true
 * @author SotirisSapak
 * @since 1.0.0
 */
inline fun <reified VB: ViewBinding> FragmentBase<VB>.attachSharedElementTransition(
    scrimThemeColor: Int,
    durationMilli: Long = 400L,
    withArchMotion: Boolean = false
) {
    sharedElementEnterTransition = MaterialContainerTransform().apply {
        scrimColor = scrimThemeColor
        duration = durationMilli
        if(withArchMotion) setPathMotion(MaterialArcMotion())
    }
}

/**
 * Use this [FragmentBase] extension to generate a new navigation trigger to another destination with
 * shared element transition in a simple yet powerful way.
 * @param viewToRegister the binding component to participate in transition (must provide the same
 * transition name to destination binding component to work)
 * @param idToNavigate the destination id to provide to [NavController].
 * @author SotirisSapak
 * @since 1.0.0
 */
inline fun <reified VB: ViewBinding> FragmentBase<VB>.navigateWithSharedElementTransition(
    viewToRegister: View,
    @IdRes idToNavigate: Int
) {
    val extras = FragmentNavigatorExtras(
        viewToRegister to viewToRegister.transitionName
    )
    findNavController().navigate(idToNavigate, null, null, extras)
}

/**
 * Use this [FragmentBase] extension to generate a new navigation trigger to another destination with
 * shared element transition in a simple yet powerful way.
 * @param viewToRegister the binding component to participate in transition (must provide the same
 * transition name to destination binding component to work)
 * @param idToNavigate the destination id to provide to [NavController].
 * @param args arguments to pass data to another fragment
 * @author SotirisSapak
 * @since 0.0.5
 */
inline fun <reified VB: ViewBinding> FragmentBase<VB>.navigateWithSharedElementTransition(
    viewToRegister: View,
    args: Bundle,
    @IdRes idToNavigate: Int
) {
    val extras = FragmentNavigatorExtras(
        viewToRegister to viewToRegister.transitionName
    )
    findNavController().navigate(idToNavigate, args, null, extras)
}

/**
 * Use this [FragmentBase] extension to generate a new navigation trigger to another destination.
 * @param idToNavigate the destination id to provide to [NavController]
 * @author SotirisSapak
 * @since 0.0.2
 */
inline fun <reified VB: ViewBinding> FragmentBase<VB>.navigate(
    @IdRes idToNavigate: Int
) {
    findNavController().navigate(idToNavigate)
}

/**
 * Use this [FragmentBase] extension to generate a new navigation trigger to another destination.
 * @param idToNavigate the destination id to provide to [NavController]
 * @param args the bundle arguments to pass to next fragment
 * @author SotirisSapak
 * @since 0.0.5
 */
inline fun <reified VB: ViewBinding> FragmentBase<VB>.navigate(
    @IdRes idToNavigate: Int,
    args: Bundle
) {
    findNavController().navigate(idToNavigate, args)
}