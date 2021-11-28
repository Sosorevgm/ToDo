package com.sosorevgm.todo.di.modules

import android.content.Context
import com.sosorevgm.todo.di.scopes.AppScope
import com.sosorevgm.todo.domain.api.TasksApi
import com.sosorevgm.todo.domain.background.WorkerManagerImpl
import com.sosorevgm.todo.domain.cache.TasksDao
import com.sosorevgm.todo.domain.cache.TasksToSynchronizeDao
import com.sosorevgm.todo.features.main.SynchronizeTasksRepository
import com.sosorevgm.todo.features.main.SynchronizeTasksRepositoryImpl
import com.sosorevgm.todo.features.main.SynchronizeTasksUseCaseImpl
import com.sosorevgm.todo.features.tasks.TasksRepository
import com.sosorevgm.todo.features.tasks.TasksRepositoryImpl
import com.sosorevgm.todo.features.tasks.TasksUseCaseImpl
import dagger.Module
import dagger.Provides

@Module
class AppModule {

    @AppScope
    @Provides
    fun providesWorkerManagerImpl(
        context: Context
    ): WorkerManagerImpl = WorkerManagerImpl(context)

    @AppScope
    @Provides
    fun providesTasksUseCaseImpl(
        repository: TasksRepository
    ): TasksUseCaseImpl = TasksUseCaseImpl(repository)

    @AppScope
    @Provides
    fun providesTasksRepository(
        tasksDao: TasksDao,
        tasksToSynchronizeDao: TasksToSynchronizeDao
    ): TasksRepositoryImpl = TasksRepositoryImpl(tasksDao, tasksToSynchronizeDao)

    @AppScope
    @Provides
    fun providesSynchronizeUseCaseImpl(
        repository: SynchronizeTasksRepository
    ): SynchronizeTasksUseCaseImpl = SynchronizeTasksUseCaseImpl(repository)

    @AppScope
    @Provides
    fun providesSynchronizeRepositoryImpl(
        tasksDao: TasksDao,
        tasksToSynchronizeDao: TasksToSynchronizeDao,
        api: TasksApi
    ): SynchronizeTasksRepositoryImpl =
        SynchronizeTasksRepositoryImpl(tasksDao, tasksToSynchronizeDao, api)
}