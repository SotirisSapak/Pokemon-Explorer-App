package com.sotirisapak.libs.pokemonexplorer.framework

import android.content.res.Resources
import android.util.DisplayMetrics

/**
 * Convert the Dp int value to px based on device pixel density
 * @author SotirisSapak
 * @since 1.0.0
 */
val Int.px: Int
    get() = this / (Resources.getSystem().displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)

/**
 * Convert the pixel int value to dp based on device pixel density
 * @author SotirisSapak
 * @since 1.0.0
 */
val Int.dp: Int
    get() = this * (Resources.getSystem().displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)