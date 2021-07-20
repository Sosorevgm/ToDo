package com.sosorevgm.todo.di.modules

import com.sosorevgm.todo.di.scopes.AppScope
import com.sosorevgm.todo.domain.background.WorkerManager
import com.sosorevgm.todo.domain.background.WorkerManagerImpl
import dagger.Binds
import dagger.Module

@Module
abstract class WorkerManagerModule {

    @AppScope
    @Binds
    abstract fun bindsWorkerManager(workerManager: WorkerManagerImpl): WorkerManager
}