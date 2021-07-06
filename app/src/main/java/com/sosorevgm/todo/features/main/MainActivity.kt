package com.sosorevgm.todo.features.main

import android.os.Bundle
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.sosorevgm.todo.R
import com.sosorevgm.todo.domain.background.TasksWorker
import dagger.android.support.DaggerAppCompatActivity
import java.util.concurrent.TimeUnit

class MainActivity : DaggerAppCompatActivity() {

    companion object {
        private const val TASK_WORKER_TAG = "task.worker.tag"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tasksWorkRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<TasksWorker>(1, TimeUnit.HOURS).build()
        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            TASK_WORKER_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            tasksWorkRequest
        )
    }
}