package com.sosorevgm.todo.di.modules

import com.sosorevgm.todo.di.scopes.AppScope
import com.sosorevgm.todo.domain.background.TasksWorkerFactory
import com.sosorevgm.todo.domain.notifications.PushManager
import com.sosorevgm.todo.features.main.SynchronizeTasksUseCase
import com.sosorevgm.todo.features.tasks.TasksUseCase
import dagger.Module
import dagger.Provides

@Module
class WorkerFactoryModule {

    @AppScope
    @Provides
    fun providesWorkerFactory(
        tasksUseCase: TasksUseCase,
        syncUseCase: SynchronizeTasksUseCase,
        pushManager: PushManager
    ): TasksWorkerFactory = TasksWorkerFactory(tasksUseCase, syncUseCase, pushManager)
}