package com.sosorevgm.todo.features.main

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.sosorevgm.todo.R
import com.sosorevgm.todo.domain.background.WorkerManager
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

class MainActivity : DaggerAppCompatActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var workerManager: WorkerManager

    private val viewModel by lazy(mode = LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory)
            .get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.startSynchronizingTasks()
        workerManager.startWorkers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkTasksUpdate()
    }
}