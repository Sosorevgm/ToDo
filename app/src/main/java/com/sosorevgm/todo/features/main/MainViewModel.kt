package com.sosorevgm.todo.features.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sosorevgm.todo.domain.account.AccountManager
import com.sosorevgm.todo.domain.background.WorkerManager
import com.sosorevgm.todo.domain.cache.TaskSynchronizeEntity
import com.sosorevgm.todo.domain.cache.toTaskApiModels
import com.sosorevgm.todo.models.TaskSynchronizeAction
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val accountManager: AccountManager,
    private val workerManager: WorkerManager,
    private val synchronizeTasksUseCase: SynchronizeTasksUseCase
) : ViewModel() {

    // enum class for control MainActivity recreation
    private enum class WorkState {
        FIRST_LAUNCH,
        IN_PROGRESS
    }

    private var state = WorkState.FIRST_LAUNCH
    private var synchronizeJob: Job? = null
    private var tasksUpdateJob: Job? = null

    fun navigate() {
        if (state == WorkState.IN_PROGRESS) return
    }

    fun startWorkers() {
        if (state == WorkState.IN_PROGRESS) return
        workerManager.startWorkers()
    }

    // start new flow at first MainActivity creation
    // sendSingleTask to realtime update of single task
    // sendTasks to synchronize data if single request failed
    fun startSynchronizingTasks() {
        if (state == WorkState.IN_PROGRESS) return
        state = WorkState.IN_PROGRESS
        viewModelScope.launch {
            synchronizeTasksUseCase.getTasksToSynchronize().collect { tasksToSynchronize ->
                if (synchronizeJob == null || synchronizeJob!!.isCompleted) {
                    if (tasksToSynchronize.size == 1) {
                        synchronizeJob = launch { sendSingleTask(tasksToSynchronize[0]) }
                    } else if (tasksToSynchronize.size > 1) {
                        synchronizeJob = launch { sendTasks(tasksToSynchronize) }
                    }
                }
            }
        }
    }

    fun checkTasksUpdate() {
        if (tasksUpdateJob == null || tasksUpdateJob!!.isCompleted) {
            tasksUpdateJob = viewModelScope.launch { synchronizeTasksUseCase.checkTasksUpdate() }
        }
    }

    private suspend fun sendSingleTask(task: TaskSynchronizeEntity) {
        when (task.action) {
            TaskSynchronizeAction.ADD -> synchronizeTasksUseCase.addTask(task.toTaskApiModel())
            TaskSynchronizeAction.UPDATE -> synchronizeTasksUseCase.updateTask(task.toTaskApiModel())
            TaskSynchronizeAction.DELETE -> synchronizeTasksUseCase.deleteTask(task.toTaskApiModel())
        }
    }

    private suspend fun sendTasks(tasks: List<TaskSynchronizeEntity>) {
        val tasksToDelete = tasks.filter { it.action == TaskSynchronizeAction.DELETE }.map { it.id }
        val tasksToAdd = tasks.filter { it.action == TaskSynchronizeAction.ADD }
        val tasksToUpdate = tasks.filter { it.action == TaskSynchronizeAction.UPDATE }
        synchronizeTasksUseCase.synchronizeTasks(
            tasksToDelete,
            tasksToAdd.toTaskApiModels(),
            tasksToUpdate.toTaskApiModels()
        )
    }

}