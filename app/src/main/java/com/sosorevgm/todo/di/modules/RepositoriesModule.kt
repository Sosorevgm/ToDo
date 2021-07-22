package com.sosorevgm.todo.di.modules

import com.sosorevgm.todo.di.scopes.AppScope
import com.sosorevgm.todo.features.main.SynchronizeTasksRepository
import com.sosorevgm.todo.features.main.SynchronizeTasksRepositoryImpl
import com.sosorevgm.todo.features.tasks.TasksRepository
import com.sosorevgm.todo.features.tasks.TasksRepositoryImpl
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoriesModule {

    @AppScope
    @Binds
    abstract fun bindsTasksRepository(tasksRepository: TasksRepositoryImpl): TasksRepository

    @AppScope
    @Binds
    abstract fun bindsSynchronizeTaskRepository(synchronizeTasksRepositoryImpl: SynchronizeTasksRepositoryImpl): SynchronizeTasksRepository
}