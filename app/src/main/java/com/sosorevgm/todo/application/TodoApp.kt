package com.sosorevgm.todo.application

import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import com.sosorevgm.todo.di.DaggerAppComponent
import com.sosorevgm.todo.domain.background.TasksWorkerFactory
import com.sosorevgm.todo.domain.notifications.PushManager
import com.sosorevgm.todo.features.tasks.TasksUseCase
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

class TodoApp : DaggerApplication(), Configuration.Provider {

    @Inject
    lateinit var tasksUseCase: TasksUseCase

    @Inject
    lateinit var pushManager: PushManager

    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        val workerFactory = DelegatingWorkerFactory()
        workerFactory.addFactory(TasksWorkerFactory(tasksUseCase, pushManager))
        return Configuration.Builder().setWorkerFactory(workerFactory).build()
    }
}