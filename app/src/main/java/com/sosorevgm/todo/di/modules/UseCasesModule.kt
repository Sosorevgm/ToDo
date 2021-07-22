package com.sosorevgm.todo.di.modules

import com.sosorevgm.todo.di.scopes.AppScope
import com.sosorevgm.todo.features.main.SynchronizeTasksUseCase
import com.sosorevgm.todo.features.main.SynchronizeTasksUseCaseImpl
import com.sosorevgm.todo.features.tasks.TasksUseCase
import com.sosorevgm.todo.features.tasks.TasksUseCaseImpl
import dagger.Binds
import dagger.Module

@Module
abstract class UseCasesModule {

    @AppScope
    @Binds
    abstract fun bindsTaskUseCase(tasksUseCase: TasksUseCaseImpl): TasksUseCase

    @AppScope
    @Binds
    abstract fun bindsSynchronizeTaskUseCase(tasksToSynchronizeUseCase: SynchronizeTasksUseCaseImpl): SynchronizeTasksUseCase
}