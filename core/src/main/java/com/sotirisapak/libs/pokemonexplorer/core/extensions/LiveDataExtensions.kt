package com.sotirisapak.libs.pokemonexplorer.core.extensions

import androidx.lifecycle.MutableLiveData

// ? ///////////////////////////////////////////////////////////////////////////////////////////////
// ?              MutableLiveData clear operation for primitive types and generic T
// ? ///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Remove the value from a [MutableLiveData] and set it to [default] value
 * @param default the value to clear the [MutableLiveData] property.
 * @author SotirisSapak
 * @since 1.0.0
 */
fun MutableLiveData<String>.clear(default: String = "") = set(default)

/**
 * Remove the value from a [MutableLiveData] and set it to [default] value
 * @param default the value to clear the [MutableLiveData] property.
 * @author SotirisSapak
 * @since 1.0.0
 */
fun MutableLiveData<Int>.clear(default: Int = 0) = set(default)

/**
 * Remove the value from a [MutableLiveData] and set it to [default] value
 * @param default the value to clear the [MutableLiveData] property.
 * @author SotirisSapak
 * @since 1.0.0
 */
fun MutableLiveData<Boolean>.clear(default: Boolean = false) = set(default)

/**
 * Remove the value from a [MutableLiveData] and set it to [default] value
 * @param default the value to clear the [MutableLiveData] property.
 * @author SotirisSapak
 * @since 1.0.0
 */
fun MutableLiveData<Double>.clear(default: Double = 0.0) = set(default)

/**
 * Remove the value from a [MutableLiveData] and set it to [default] value
 * @param default the value to clear the [MutableLiveData] property.
 * @author SotirisSapak
 * @since 1.0.0
 */
fun MutableLiveData<Float>.clear(default: Float = 0f) = set(default)

/**
 * Remove the value from a [MutableLiveData] and set it to [default] value
 * @param default the value to clear the [MutableLiveData] property.
 * @author SotirisSapak
 * @since 1.0.0
 */
inline fun <reified T: Any> MutableLiveData<T>.clear(default: T? = null) = set(default)


// ? ///////////////////////////////////////////////////////////////////////////////////////////////
// ?              MutableLiveData initializers for primitive types and generic T
// ? ///////////////////////////////////////////////////////////////////////////////////////////////


/**
 * Generate a new [MutableLiveData] property by giving a default value.
 *
 * For example:
 *
 * <code>
 *
 *     val liveData = "".mutableLiveData
 *     // similar to:
 *     val liveData = MutableLiveData<String>("")
 * </code>
 * @author SotirisSapak
 * @since 1.0.0
 */
val String.mutableLiveData: MutableLiveData<String> get() = MutableLiveData(this)

/**
 * Generate a new [MutableLiveData] property by giving a default value.
 *
 * For example:
 *
 * <code>
 *
 *     val liveData = 0.mutableLiveData
 *     // similar to:
 *     val liveData = MutableLiveData<Int>(0)
 * </code>
 * @author SotirisSapak
 * @since 1.0.0
 */
val Int.mutableLiveData: MutableLiveData<Int> get() = MutableLiveData(this)

/**
 * Generate a new [MutableLiveData] property by giving a default value.
 *
 * For example:
 *
 * <code>
 *
 *     val liveData = false.mutableLiveData
 *     // similar to:
 *     val liveData = MutableLiveData<Boolean>(false)
 * </code>
 * @author SotirisSapak
 * @since 1.0.0
 */
val Boolean.mutableLiveData: MutableLiveData<Boolean> get() = MutableLiveData(this)

/**
 * Generate a new [MutableLiveData] property by giving a default value.
 *
 * For example:
 *
 * <code>
 *
 *     val liveData = 0.0.mutableLiveData
 *     // similar to:
 *     val liveData = MutableLiveData<Double>(0.0)
 * </code>
 * @author SotirisSapak
 * @since 1.0.0
 */
val Double.mutableLiveData: MutableLiveData<Double> get() = MutableLiveData(this)

/**
 * Generate a new [MutableLiveData] property by giving a default value.
 *
 * For example:
 *
 * <code>
 *
 *     val liveData = 0f.mutableLiveData
 *     // similar to:
 *     val liveData = MutableLiveData<Float>(0f)
 * </code>
 * @author SotirisSapak
 * @since 1.0.0
 */
val Float.mutableLiveData: MutableLiveData<Float> get() = MutableLiveData(this)

/**
 * Generate a new [MutableLiveData] property by giving a default value.
 *
 * For example:
 *
 * <code>
 *
 *     val liveData = Order.mutableLiveData
 *     // similar to:
 *     val liveData = MutableLiveData<Order>(Order())
 * </code>
 * @author SotirisSapak
 * @since 1.0.0
 */
inline fun <reified T: Any> T.mutableLiveData() = MutableLiveData(this)


// ? ///////////////////////////////////////////////////////////////////////////////////////////////
// ?             MutableLiveData change operation for primitive types and generic T
// ? ///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Change the value of the [MutableLiveData] to [new]
 * @param new the new value to attach to [MutableLiveData]
 * @author SotirisSapak
 * @since 1.0.0
 */
fun MutableLiveData<String>.set(new: String = "") { value = new }

/**
 * Change the value of the [MutableLiveData] to [new]
 * @param new the new value to attach to [MutableLiveData]
 * @author SotirisSapak
 * @since 1.0.0
 */
fun MutableLiveData<Int>.set(new: Int = 0) { value = new }

/**
 * Change the value of the [MutableLiveData] to [new]
 * @param new the new value to attach to [MutableLiveData]
 * @author SotirisSapak
 * @since 1.0.0
 */
fun MutableLiveData<Boolean>.set(new: Boolean = true) { value = new }

/**
 * Change the value of the [MutableLiveData] to [new]
 * @param new the new value to attach to [MutableLiveData]
 * @author SotirisSapak
 * @since 1.0.0
 */
fun MutableLiveData<Double>.set(new: Double = 0.0) { value = new }

/**
 * Change the value of the [MutableLiveData] to [new]
 * @param new the new value to attach to [MutableLiveData]
 * @author SotirisSapak
 * @since 1.0.0
 */
fun MutableLiveData<Float>.set(new: Float = 0f) { value = new }

/**
 * Change the value of the [MutableLiveData] to [new]
 * @param new the new value to attach to [MutableLiveData]
 * @author SotirisSapak
 * @since 1.0.0
 */
inline fun <reified T: Any> MutableLiveData<T>.set(new: T? = null) { value = new }


// ? ///////////////////////////////////////////////////////////////////////////////////////////////
// ?                        MutableLiveData non-nullable value return
// ? ///////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Get the non null value from a [MutableLiveData]
 * @author SotirisSapak
 * @since 1.0.0
 */
val MutableLiveData<String>.nonNullValue: String get() = this.value ?: ""

/**
 * Get the non null value from a [MutableLiveData]
 * @author SotirisSapak
 * @since 1.0.0
 */
val MutableLiveData<Int>.nonNullValue: Int get() = this.value ?: 0

/**
 * Get the non null value from a [MutableLiveData]
 * @author SotirisSapak
 * @since 1.0.0
 */
val MutableLiveData<Boolean>.nonNullValue: Boolean get() = this.value ?: false

/**
 * Get the non null value from a [MutableLiveData]
 * @author SotirisSapak
 * @since 1.0.0
 */
val MutableLiveData<Double>.nonNullValue: Double get() = this.value ?: 0.0

/**
 * Get the non null value from a [MutableLiveData]
 * @author SotirisSapak
 * @since 1.0.0
 */
val MutableLiveData<Float>.nonNullValue: Float get() = this.value ?: 0f