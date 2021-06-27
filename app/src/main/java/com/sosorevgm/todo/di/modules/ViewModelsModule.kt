package com.sosorevgm.todo.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sosorevgm.todo.di.factory.ViewModelKey
import com.sosorevgm.todo.di.factory.ViewModelProviderFactory
import com.sosorevgm.todo.features.main.MainViewModel
import com.sosorevgm.todo.features.new_task.NewTaskViewModel
import com.sosorevgm.todo.features.tasks.TasksViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    @Binds
    abstract fun bindViewModelFactory(
        factory: ViewModelProviderFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun providesMainViewModel(
        viewModel: MainViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TasksViewModel::class)
    abstract fun providesTasksViewModel(
        viewModel: TasksViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(NewTaskViewModel::class)
    abstract fun providesNewTaskViewModel(
        viewModel: NewTaskViewModel
    ): ViewModel
}