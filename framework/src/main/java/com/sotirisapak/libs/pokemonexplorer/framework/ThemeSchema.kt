package com.sotirisapak.libs.pokemonexplorer.framework

import android.app.Activity
import android.content.Context
import android.util.TypedValue
import com.google.android.material.R

/**
 * Master component for fetching the material theme schema colors
 * @author SotirisSapak
 * @since 1.0.0
 */
class ThemeSchema {

    /**
     * All the available colors to fetch from schema
     * @author SotirisSapak
     * @since 1.0.0
     */
    companion object Colors {

        val primary = R.attr.colorPrimary
        val primaryContainer = R.attr.colorPrimaryContainer
        val primaryDark = R.attr.colorPrimaryDark
        val primaryFixed = R.attr.colorPrimaryFixed
        val primaryFixedDim = R.attr.colorPrimaryFixedDim
        val primaryInverse = R.attr.colorPrimaryInverse
        val primarySurface = R.attr.colorPrimarySurface
        val primaryVariant = R.attr.colorPrimaryVariant
        val onPrimary = R.attr.colorOnPrimary
        val onPrimaryContainer = R.attr.colorOnPrimaryContainer
        val onPrimaryFixed = R.attr.colorOnPrimaryFixed
        val onPrimaryFixedVariant = R.attr.colorOnPrimaryFixedVariant
        val onPrimarySurface = R.attr.colorOnPrimarySurface

        val secondary = R.attr.colorSecondary
        val secondaryContainer = R.attr.colorSecondaryContainer
        val secondaryFixed = R.attr.colorSecondaryFixed
        val secondaryFixedDim = R.attr.colorSecondaryFixedDim
        val secondaryVariant = R.attr.colorSecondaryVariant
        val onSecondary = R.attr.colorOnSecondary
        val onSecondaryContainer = R.attr.colorOnSecondaryContainer
        val onSecondaryFixed = R.attr.colorOnSecondaryFixed
        val onSecondaryFixedVariant = R.attr.colorOnSecondaryFixedVariant

        val tertiary = R.attr.colorTertiary
        val tertiaryContainer = R.attr.colorTertiaryContainer
        val tertiaryFixed = R.attr.colorTertiaryFixed
        val tertiaryFixedDim = R.attr.colorTertiaryFixedDim
        val onTertiary = R.attr.colorOnTertiary
        val onTertiaryContainer = R.attr.colorOnTertiaryContainer
        val onTertiaryFixed = R.attr.colorOnTertiaryFixed
        val onTertiaryFixedVariant = R.attr.colorOnTertiaryFixedVariant

        val error = R.attr.colorError
        val errorContainer = R.attr.colorErrorContainer
        val onError = R.attr.colorOnError
        val onErrorContainer = R.attr.colorOnErrorContainer

        val surface = R.attr.colorSurface
        val surfaceBright = R.attr.colorSurfaceBright
        val surfaceContainer = R.attr.colorSurfaceContainer
        val surfaceContainerHigh = R.attr.colorSurfaceContainerHigh
        val surfaceContainerHighest = R.attr.colorSurfaceContainerHighest
        val surfaceContainerLow = R.attr.colorSurfaceContainerLow
        val surfaceContainerLowest = R.attr.colorSurfaceContainerLowest
        val surfaceDim = R.attr.colorSurfaceDim
        val surfaceInverse = R.attr.colorSurfaceInverse
        val surfaceVariant = R.attr.colorSurfaceVariant
        val onSurface = R.attr.colorOnSurface
        val onSurfaceInverse = R.attr.colorOnSurfaceInverse
        val onSurfaceVariant = R.attr.colorOnSurfaceVariant

        val background = R.attr.backgroundColor
        val onBackground = R.attr.colorOnBackground

        val transparent = android.R.color.transparent

        /**
         * Get the selected color
         * @param context the context
         * @param color one of the [ThemeSchema] colors
         * @return the color int value
         * @author SotirisSapak
         * @since 1.0.0
         */
        @JvmStatic fun get(context: Context, color: Int): Int {
            val typeValue = TypedValue()
            val intArray = intArrayOf(color)
            val styledAttributes = context.obtainStyledAttributes(typeValue.data, intArray)
            val mColor = styledAttributes.getColor(0,0)
            styledAttributes.recycle()
            return mColor
        }

    }

}

////////////////////////////////////////////////////////////////////////////////////////////////////
//                                    DATA BINDING OBJECT
////////////////////////////////////////////////////////////////////////////////////////////////////
object ThemeDataBinding {
    @JvmStatic fun getColor(context: Context, id: Int) = context.getSchemaColor(id)
}

////////////////////////////////////////////////////////////////////////////////////////////////////
//                                        EXTENSIONS
////////////////////////////////////////////////////////////////////////////////////////////////////

/**
 * Get schema color by using the default context. Can be used in fragments, activities and any
 * context instances.
 * @param color the color id from [ThemeSchema].
 * @return the theme color
 * @author SotirisSapak
 * @since 1.0.0
 */
fun Context.getSchemaColor(color: Int): Int {
    val typeValue = TypedValue()
    val intArray = intArrayOf(color)
    val styledAttributes = this.obtainStyledAttributes(typeValue.data, intArray)
    val mColor = styledAttributes.getColor(0,0)
    styledAttributes.recycle()
    return mColor
}