package com.sotirisapak.libs.pokemonexplorer.framework

import android.content.res.Resources
import android.util.DisplayMetrics

/**
 * Convert the Dp int value to px based on device pixel density
 * @author SotirisSapak
 * @since 1.0.0
 */
val Int.dp: Int
    get() = this / (Resources.getSystem().displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)

/**
 * Convert the pixel int value to dp based on device pixel density
 * @author SotirisSapak
 * @since 1.0.0
 */
val Int.px: Int
    get() = this * (Resources.getSystem().displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)

/**
 * DotPoints converter to use in any xml data binding view
 * @author SotirisSapak
 * @since 1.0.0
 */
object DP {

    /**
     * Convert any px metric to dp
     * @param dp the final dp result
     * @author SotirisSapak
     * @since 1.0.0
     */
    @JvmStatic
    fun fromPx(dp: Int) =
        dp * Resources.getSystem().displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT

}