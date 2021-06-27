package com.sosorevgm.todo.di

import android.app.Application
import com.sosorevgm.todo.application.TodoApp
import com.sosorevgm.todo.di.modules.*
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBuildersModule::class,
        AppModule::class,
        ViewModelsModule::class,
        CacheModule::class,
        UseCasesModule::class,
        RepositoriesModule::class,
        AccountModule::class,
        NotificationsModule::class
    ]
)
interface AppComponent : AndroidInjector<TodoApp> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}