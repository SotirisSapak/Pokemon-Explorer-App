plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    // * data binding and room plugin kapt
    id("kotlin-kapt")
}

android {
    namespace = "$libraryPkg.backend"
    compileSdk = libs.versions.sdk.target.get().toInt()

    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
        buildFeatures { dataBinding = true }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = libs.versions.java.get().javaVersion
        targetCompatibility = libs.versions.java.get().javaVersion
    }
    kotlinOptions {
        jvmTarget = libs.versions.java.get()
    }
}

dependencies {
    // ? Android core features
    implementation(libs.androidx.core.ktx)
    // ? Android appCompat support
    implementation(libs.androidx.appcompat)
    // ? Material library
    implementation(libs.material)
    // ? Retrofit library for HTTP request abstraction
    implementation(libs.retrofit)
    // ? Retrofit JSON converter library
    implementation(libs.retrofit.converter.json)
    // ? For room database
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.room.compiler)    // not using KSP due to lack of dataBinding support
    // ? Local libraries dependencies
    implementation(project(":core"))
    // ? Testing libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// ? configure kapt engine
kapt { correctErrorTypes = true }