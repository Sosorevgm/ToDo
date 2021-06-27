package com.sosorevgm.todo.di.modules

import com.sosorevgm.todo.features.tasks.TasksRepository
import com.sosorevgm.todo.features.tasks.TasksRepositoryImpl
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoriesModule {

    @Singleton
    @Binds
    abstract fun bindsTasksRepository(tasksRepository: TasksRepositoryImpl): TasksRepository
}