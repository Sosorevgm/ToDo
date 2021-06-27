package com.sosorevgm.todo.di.modules

import com.sosorevgm.todo.features.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @ContributesAndroidInjector(
        modules = [
            FragmentBuildersModule::class
        ]
    )
    abstract fun contributeMainActivity(): MainActivity
}