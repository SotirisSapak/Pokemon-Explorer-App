package com.sotirisapak.libs.pokemonexplorer.framework

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding


/** No insets applied to [View] component if attach this property to any inset type */
const val noInsets = -1

/** system insets applied to [View] component if attach this property to any inset type */
const val systemInsets = 0

/**
 * The insets type
 * @author SotirisSapak
 * @since 1.0.0
 */
enum class InsetType {
    /** Status and navigation bar insets */
    SystemBars,
    /** System gestures insets */
    Gestures,
    /** StatusBar only insets */
    StatusBar,
    /** NavigationBar only insets */
    NavBar
}

/**
 * __Extension__
 *
 * The default function to add insets to any component.
 * @param start the start side of the component (left)
 * @param top the top side of the component
 * @param end the end side of the component (right)
 * @param bottom the bottom side of the component
 * @param type the system inset type preset
 * @author SotirisSapak
 * @since 1.0.0
 */
fun View.insets(
    start: Int = noInsets,
    top: Int = noInsets,
    end: Int = noInsets,
    bottom: Int = noInsets,
    type: InsetType = InsetType.SystemBars
) {

    /** insets typeMask to use when applying window insets */
    val typeMask = when(type){
        InsetType.SystemBars -> WindowInsetsCompat.Type.systemBars()
        InsetType.Gestures -> WindowInsetsCompat.Type.systemGestures()
        InsetType.StatusBar -> WindowInsetsCompat.Type.statusBars()
        InsetType.NavBar -> WindowInsetsCompat.Type.navigationBars()
    }

    ViewCompat.setOnApplyWindowInsetsListener(this) { v, windowInsets ->
        // get the default system gestures insets
        val insets = windowInsets.getInsets(typeMask)
        val startInset = if(start < systemInsets) 0 else if(start == systemInsets) insets.left else insets.left + start.dp
        val topInset = if(top < systemInsets) 0 else if(top == systemInsets) insets.top else insets.top + top.dp
        val endInset = if(end < systemInsets) 0 else if(end == systemInsets) insets.right else insets.right + end.dp
        val bottomInset = if(bottom < systemInsets) 0 else if(bottom == systemInsets) insets.bottom else insets.bottom + bottom.dp
        // based on the attributes we have to update the padding
        v.updatePadding(
            left = startInset,
            top = topInset,
            right = endInset,
            bottom = bottomInset,
        )

        windowInsets
    }
}

/**
 * System insets to apply to component that is at the top of the screen.
 *
 * Insets applied to:
 *
 * <code>
 *
 *     +=====================================+
 *     ||                                   ||
 *     ||             COMPONENT             ||
 *     ||                                   ||
 *     +-------------------------------------+
 *
 * </code>
 * @author SotirisSapak
 * @since 1.0.0
 */
fun View.topRoundedInsets() = insets(systemInsets, systemInsets, systemInsets, noInsets)

/**
 * System insets to apply to component that is at the center of the screen.
 *
 * Insets applied to:
 *
 * <code>
 *
 *     +-------------------------------------+
 *     ||                                   ||
 *     ||             COMPONENT             ||
 *     ||                                   ||
 *     +-------------------------------------+
 *
 * </code>
 * @author SotirisSapak
 * @since 1.0.0
 */
fun View.sideInsets() = insets(systemInsets, noInsets, systemInsets, noInsets)

/**
 * System insets to apply to component that is at the center of the screen.
 *
 * Insets applied to:
 *
 * <code>
 *
 *     +-------------------------------------+
 *     ||                                   ||
 *     ||             COMPONENT             ||
 *     ||                                   ||
 *     +-------------------------------------+
 *
 * </code>
 *
 * @param start the custom start insets
 * @param end the custom end insets
 * @author SotirisSapak
 * @since 1.0.0
 */
fun View.sideInsets(
    start: Int = systemInsets,
    end: Int = systemInsets
) = insets(start = start, end = end)

/**
 * System insets to apply to component that is at the bottom of the screen.
 *
 * Insets applied to:
 *
 * <code>
 *
 *     +-------------------------------------+
 *     ||                                   ||
 *     ||             COMPONENT             ||
 *     ||                                   ||
 *     +=====================================+
 *
 * </code>
 * @author SotirisSapak
 * @since 1.0.0
 */
fun View.bottomRoundedInsets(
    start: Int = systemInsets,
    top: Int = noInsets,
    end: Int = systemInsets,
    bottom: Int = systemInsets
) = insets(start, top, end, bottom)
