package com.sosorevgm.todo.domain.background

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface WorkerManager {
    fun startWorkers()
}

class WorkerManagerImpl @Inject constructor(
    private val context: Context
) : WorkerManager {

    companion object {
        private const val NOTIFICATIONS_WORKER_TAG = "notifications.worker.tag"
        private const val SYNCHRONIZATION_WORKER_TAG = "synchronization.worker.tag"
    }

    override fun startWorkers() {
        startNotificationWorker()
        startTaskSynchronizationWorker()
    }

    private fun startNotificationWorker() {
        val tasksWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<TasksNotificationsWorker>(1, TimeUnit.HOURS).build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            NOTIFICATIONS_WORKER_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            tasksWorkRequest
        )
    }

    private fun startTaskSynchronizationWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val tasksSynchronizationWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<TasksSynchronizationWorker>(
                8,
                TimeUnit.HOURS
            ).setConstraints(constraints).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            SYNCHRONIZATION_WORKER_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            tasksSynchronizationWorkRequest
        )
    }
}