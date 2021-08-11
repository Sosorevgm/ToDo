package com.sosorevgm.todo.application

import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import com.sosorevgm.todo.di.components.DaggerAppComponent
import com.sosorevgm.todo.domain.background.TasksWorkerFactory
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber
import javax.inject.Inject

class TodoApp : DaggerApplication(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: TasksWorkerFactory

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.factory().create(this)
    }

    override fun getWorkManagerConfiguration(): Configuration {
        val factory = DelegatingWorkerFactory()
        factory.addFactory(workerFactory)
        return Configuration.Builder().setWorkerFactory(factory).build()
    }
}