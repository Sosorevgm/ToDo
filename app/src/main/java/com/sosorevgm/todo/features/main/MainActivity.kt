package com.sosorevgm.todo.features.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.sosorevgm.todo.R
import com.sosorevgm.todo.domain.background.TasksSynchronizationWorker
import com.sosorevgm.todo.domain.background.TasksWorker
import dagger.android.support.DaggerAppCompatActivity
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    companion object {
        private const val TASK_WORKER_TAG = "task.worker.tag"
        private const val TASKS_SYNCHRONIZATION_WORKER_TAG = "tasks.synchronization.worker.tag"
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)
            .get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.startSynchronizingTasks()
        startNotificationWorker()
        startTaskSynchronizationWorker()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkTasksUpdate()
    }

    private fun startNotificationWorker() {
        val tasksWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<TasksWorker>(1, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            TASK_WORKER_TAG,
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

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            TASKS_SYNCHRONIZATION_WORKER_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            tasksSynchronizationWorkRequest
        )
    }
}