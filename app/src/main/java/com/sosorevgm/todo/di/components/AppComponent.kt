package com.sosorevgm.todo.di.components

import android.content.Context
import com.sosorevgm.todo.application.TodoApp
import com.sosorevgm.todo.di.modules.*
import com.sosorevgm.todo.di.scopes.AppScope
import com.sosorevgm.todo.features.authorize.AuthorizeFragment
import com.sosorevgm.todo.features.main.MainActivity
import com.sosorevgm.todo.features.new_task.NewTaskFragment
import com.sosorevgm.todo.features.registration.RegistrationFragment
import com.sosorevgm.todo.features.tasks.TasksFragment
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule

@AppScope
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ActivityBuildersModule::class,
        AppModule::class,
        ViewModelsModule::class,
        CacheModule::class,
        RetrofitModule::class,
        UseCasesModule::class,
        RepositoriesModule::class,
        AccountModule::class,
        NotificationsModule::class,
        WorkerManagerModule::class,
        WorkerFactoryModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(app: TodoApp)
    fun inject(authorizationFragment: AuthorizeFragment)
    fun inject(registrationFragment: RegistrationFragment)
    fun inject(mainActivity: MainActivity)
    fun inject(taskFragment: TasksFragment)
    fun inject(newTaskFragment: NewTaskFragment)
}