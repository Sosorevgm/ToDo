package com.sosorevgm.todo.domain.background

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.sosorevgm.todo.domain.notifications.PushManager
import com.sosorevgm.todo.features.tasks.TasksUseCase

class TasksWorkerFactory(
    private val tasksUseCase: TasksUseCase,
    private val pushManager: PushManager
) : WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? = when (workerClassName) {
        TasksWorker::class.java.name -> TasksWorker(
            appContext,
            workerParameters,
            tasksUseCase,
            pushManager
        )
        else -> null
    }
}