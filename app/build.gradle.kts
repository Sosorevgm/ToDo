plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(Config.compile_sdk)
    buildToolsVersion(Config.build_tools_version)

    defaultConfig {
        applicationId = Config.application_id
        minSdkVersion(Config.min_sdk)
        targetSdkVersion(Config.target_sdk)
        versionCode = Release.version_code
        versionName = Release.version_name

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // default authorization token field
        buildConfigField(
            "String",
            "DEFAULT_AUTHORIZATION_TOKEN",
            "\"7b65508b61304a87ae125b3d2dbd7497\""
        )
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(Kotlin.core)
    implementation(Kotlin.std_lib)

    implementation(Design.appcompat)
    implementation(Design.material)
    implementation(Design.constraint_layout)

    implementation(Navigation.fragment_ktx)
    implementation(Navigation.ui_ktx)
    implementation(Navigation.dynamic_features_fragment)

    implementation(Coroutines.core)
    implementation(Coroutines.android)

    implementation(Lifecycle.view_model)
    implementation(Lifecycle.runtime)
    implementation(Lifecycle.extensions)
    implementation(Lifecycle.legacy_support)
    kapt(Lifecycle.compiler)

    implementation(Work.runtime)

    implementation(OkHttp.bom)
    implementation(OkHttp.okhttp)
    implementation(OkHttp.logging_interceptor)

    implementation(Retrofit.core)
    implementation(Retrofit.converter_gson)

    implementation(Room.runtime)
    implementation(Room.ktx)
    kapt(Room.compiler)

    implementation(Dagger.core)
    implementation(Dagger.android)
    implementation(Dagger.android_support)
    kapt(Dagger.compiler)
    kapt(Dagger.android_processor)

    implementation(Json.google_gson)

    implementation(Timber.core)

    testImplementation(Tests.junit_core)
    androidTestImplementation(Tests.junit_ext)
    androidTestImplementation(Tests.espresso_core)
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
}