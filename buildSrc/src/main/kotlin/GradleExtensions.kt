/*
*  Extension methods for Gradle build scripts
*/

import org.gradle.api.JavaVersion

/**
 * Convert any string from build gradle to java version.
 * Will be used to convert the libs.version java version to custom [JavaVersion] component.
 *
 * Currently, this method support java 21 max
 * @author SotirisSapak
 * @since 1.0.0
 */
fun String.asJavaVersion(): JavaVersion {
    return when(this){
        "1.8" -> JavaVersion.VERSION_1_8
        "1.9" -> JavaVersion.VERSION_1_9
        "1.10" -> JavaVersion.VERSION_1_10
        "11" -> JavaVersion.VERSION_11
        "12" -> JavaVersion.VERSION_12
        "13" -> JavaVersion.VERSION_13
        "14" -> JavaVersion.VERSION_14
        "15" -> JavaVersion.VERSION_15
        "16" -> JavaVersion.VERSION_16
        "17" -> JavaVersion.VERSION_17
        "18" -> JavaVersion.VERSION_18
        "19" -> JavaVersion.VERSION_19
        "20" -> JavaVersion.VERSION_20
        "21" -> JavaVersion.VERSION_21
        else -> JavaVersion.VERSION_1_8
    }
}

/**
 * Similar functionality to [String.asJavaVersion] method but with variable converter
 * @author SotirisSapak
 * @since 1.0.0
 */
val String.javaVersion: JavaVersion
    get() = when(this){
        "1.8" -> JavaVersion.VERSION_1_8
        "1.9" -> JavaVersion.VERSION_1_9
        "1.10" -> JavaVersion.VERSION_1_10
        "11" -> JavaVersion.VERSION_11
        "12" -> JavaVersion.VERSION_12
        "13" -> JavaVersion.VERSION_13
        "14" -> JavaVersion.VERSION_14
        "15" -> JavaVersion.VERSION_15
        "16" -> JavaVersion.VERSION_16
        "17" -> JavaVersion.VERSION_17
        "18" -> JavaVersion.VERSION_18
        "19" -> JavaVersion.VERSION_19
        "20" -> JavaVersion.VERSION_20
        "21" -> JavaVersion.VERSION_21
        else -> JavaVersion.VERSION_1_8
    }