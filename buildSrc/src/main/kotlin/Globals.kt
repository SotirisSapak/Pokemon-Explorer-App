/*
* All global attributes for gradle scripts
*/

/** The master package */
const val pkg = "com.sotirisapak"

/** The application full package (use as is) */
const val appPkg = "$pkg.apps.pokemonexplorer"

/**
 * Use this package as prefix for any library
 *
 * Example:
 *
 * <code>
 *
 *      // gradle.kts file
 *      ...
 *      val packageName = "libraryPkg.package"
 *      ...
 * </code>
 */
const val libraryPkg = "$pkg.libs.pokemonexplorer"