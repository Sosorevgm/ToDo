package com.sosorevgm.todo.application

import android.app.Application
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import com.sosorevgm.todo.di.components.AppComponent
import com.sosorevgm.todo.di.components.DaggerAppComponent
import com.sosorevgm.todo.domain.background.TasksWorkerFactory
import timber.log.Timber
import javax.inject.Inject

class TodoApp : Application(), Configuration.Provider {

    lateinit var appComponent: AppComponent

    @Inject
    lateinit var workerFactory: TasksWorkerFactory

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
        appComponent.inject(this)
        Timber.plant(Timber.DebugTree())
    }

    override fun getWorkManagerConfiguration(): Configuration {
        val factory = DelegatingWorkerFactory()
        factory.addFactory(workerFactory)
        return Configuration.Builder().setWorkerFactory(factory).build()
    }
}