plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    // * data binding and room plugin kapt
    id("kotlin-kapt")
}

android {
    namespace = "$libraryPkg.core"
    compileSdk = libs.versions.sdk.target.get().toInt()

    defaultConfig {
        minSdk = libs.versions.sdk.min.get().toInt()
        buildFeatures { dataBinding = true }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
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
    // ? Basic android activity support
    implementation(libs.androidx.activity)
    // ? LiveData support
    implementation(libs.livedata.ktx)
    // ? ViewModel support
    implementation(libs.viewmodel.ktx)
    // ? Kotlin coroutines support
    implementation(libs.coroutines.android)
    // ? Kotlin coroutines core features
    implementation(libs.coroutines.android.core)
    // ? Kotlin coroutines test support
    implementation(libs.coroutines.test)
    // ? Basic android fragment support
    implementation(libs.fragment.ktx)
    // ? Basic android navigation ui support for android studio auto-generated code
    implementation(libs.navigation.ui.ktx)
    // ? Basic android navigation for fragment support
    implementation(libs.navigation.fragment.ktx)
    // ? Kotlin reflection utilities
    implementation(libs.kotlin.reflection)
    // ? Retrofit library for HTTP request abstraction
    implementation(libs.retrofit)
    // ? Retrofit JSON converter library
    implementation(libs.retrofit.converter.json)
    // ? For room database
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.room.compiler)    // not using KSP due to lack of dataBinding support
    // ? Testing libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// ? configure kapt engine
kapt { correctErrorTypes = true }