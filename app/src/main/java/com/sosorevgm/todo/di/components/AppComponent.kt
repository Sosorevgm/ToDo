package com.sosorevgm.todo.di.components

import android.app.Application
import com.sosorevgm.todo.application.TodoApp
import com.sosorevgm.todo.di.modules.*
import com.sosorevgm.todo.di.scopes.AppScope
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@AppScope
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