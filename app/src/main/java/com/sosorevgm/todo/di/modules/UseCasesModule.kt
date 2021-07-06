package com.sosorevgm.todo.di.modules

import com.sosorevgm.todo.di.scopes.AppScope
import com.sosorevgm.todo.features.tasks.TasksUseCase
import com.sosorevgm.todo.features.tasks.TasksUseCaseImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class UseCasesModule {

    @AppScope
    @Binds
    abstract fun bindsTaskUseCase(tasksUseCase: TasksUseCaseImpl): TasksUseCase
}