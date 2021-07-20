package com.sosorevgm.todo.domain.background

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sosorevgm.todo.features.main.SynchronizeTasksUseCase
import timber.log.Timber

class TasksSynchronizationWorker(
    appContext: Context,
    workerParameters: WorkerParameters,
    private val synchronizeTasksUseCase: SynchronizeTasksUseCase
) : CoroutineWorker(appContext, workerParameters) {

    override suspend fun doWork(): Result {
        synchronizeTasksUseCase.checkTasksUpdate()
        return Result.retry()
    }
}