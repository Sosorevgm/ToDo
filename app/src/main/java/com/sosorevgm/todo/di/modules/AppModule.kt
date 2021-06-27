package com.sosorevgm.todo.di.modules

import android.app.Application
import android.content.Context
import com.sosorevgm.todo.domain.cache.TasksDao
import com.sosorevgm.todo.features.tasks.TasksRepository
import com.sosorevgm.todo.features.tasks.TasksRepositoryImpl
import com.sosorevgm.todo.features.tasks.TasksUseCaseImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun providesContext(application: Application): Context = application.applicationContext

    @Singleton
    @Provides
    fun providesTasksUseCaseImpl(
        repository: TasksRepository
    ): TasksUseCaseImpl = TasksUseCaseImpl(repository)

    @Singleton
    @Provides
    fun providesTasksRepository(
        cache: TasksDao
    ): TasksRepositoryImpl = TasksRepositoryImpl(cache)
}