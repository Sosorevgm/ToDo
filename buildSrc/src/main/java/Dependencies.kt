object Versions {
    const val gradle = "4.2.2"
    const val kotlin = "1.5.0"
    const val navigation_safe_args = "2.3.5"

    const val kotlin_core = "1.6.0"
    const val kotlin_std_lib = "1.4.10"

    const val design_appcompat = "1.3.0"
    const val design_material = "1.4.0"
    const val design_constraint_layout = "2.0.4"

    const val navigation = "2.3.5"

    const val coroutines_core = "1.5.0"
    const val coroutines_android = "1.4.3"

    const val lifecycle_view_model = "2.3.1"
    const val lifecycle_runtime = "2.3.1"
    const val lifecycle_extensions = "2.2.0"
    const val lifecycle_legacy_support = "1.0.0"
    const val lifecycle_compiler = "2.3.1"

    const val work_runtime = "2.5.0"

    const val okhttp_bom = "4.9.1"

    const val retrofit_core = "2.9.0"
    const val retrofit_converter_gson = "2.9.0"

    const val room = "2.3.0"

    const val dagger_core = "2.35.1"
    const val dagger_android = "2.35.1"
    const val dagger_android_support = "2.31.2"
    const val dagger_compiler = "2.31.2"
    const val dagger_android_processor = "2.31.2"

    const val google_gson = "2.8.6"

    const val timber = "4.7.1"

    const val junit_core = "4.13.2"
    const val junit_ext = "1.1.3"
    const val espresso_core = "3.4.0"
}

object Config {
    const val application_id = "com.sosorevgm.todo"
    const val compile_sdk = 30
    const val min_sdk = 21
    const val target_sdk = 30
    const val build_tools_version = "30.0.3"
}

object Release {
    const val version_code = 1
    const val version_name = "1.0"
}

object Kotlin {
    const val core = "androidx.core:core-ktx:${Versions.kotlin_core}"
    const val std_lib = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin_std_lib}"
}

object Design {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.design_appcompat}"
    const val material = "com.google.android.material:material:${Versions.design_material}"
    const val constraint_layout =
        "androidx.constraintlayout:constraintlayout:${Versions.design_constraint_layout}"
}

object Navigation {
    const val fragment_ktx = "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"
    const val ui_ktx = "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"
    const val dynamic_features_fragment =
        "androidx.navigation:navigation-dynamic-features-fragment:${Versions.navigation}"
}

object Coroutines {
    const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines_core}"
    const val android =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines_android}"
}

object Lifecycle {
    const val view_model =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle_view_model}"
    const val runtime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle_runtime}"
    const val extensions =
        "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle_extensions}"
    const val legacy_support =
        "androidx.legacy:legacy-support-v4:${Versions.lifecycle_legacy_support}"
    const val compiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle_compiler}"
}

object Work {
    const val runtime = "androidx.work:work-runtime-ktx:${Versions.work_runtime}"
}

object OkHttp {
    const val bom = "com.squareup.okhttp3:okhttp-bom:${Versions.okhttp_bom}"
    const val okhttp = "com.squareup.okhttp3:okhttp"
    const val logging_interceptor = "com.squareup.okhttp3:logging-interceptor"
}

object Retrofit {
    const val core = "com.squareup.retrofit2:retrofit:${Versions.retrofit_core}"
    const val converter_gson =
        "com.squareup.retrofit2:converter-gson:${Versions.retrofit_converter_gson}"
}

object Room {
    const val runtime = "androidx.room:room-runtime:${Versions.room}"
    const val ktx = "androidx.room:room-ktx:${Versions.room}"
    const val compiler = "androidx.room:room-compiler:${Versions.room}"
}

object Dagger {
    const val core = "com.google.dagger:dagger:${Versions.dagger_core}"
    const val android = "com.google.dagger:dagger-android:${Versions.dagger_android}"
    const val android_support =
        "com.google.dagger:dagger-android-support:${Versions.dagger_android_support}"
    const val compiler =
        "com.google.dagger:dagger-android-processor:${Versions.dagger_android_processor}"
    const val android_processor = "com.google.dagger:dagger-compiler:${Versions.dagger_compiler}"
}

object Json {
    const val google_gson = "com.google.code.gson:gson:${Versions.google_gson}"
}

object Timber {
    const val core = "com.jakewharton.timber:timber:${Versions.timber}"
}

object Tests {
    const val junit_core = "junit:junit:${Versions.junit_core}"
    const val junit_ext = "androidx.test.ext:junit:${Versions.junit_ext}"
    const val espresso_core = "androidx.test.espresso:espresso-core:${Versions.espresso_core}"
}