package com.sosorevgm.todo.features.tasks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sosorevgm.todo.domain.account.AccountManager
import com.sosorevgm.todo.features.tasks.recycler.TaskViewData
import com.sosorevgm.todo.models.TaskComparator
import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.switchIsDone
import com.sosorevgm.todo.models.toViewData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    private val accountManager: AccountManager,
    private val tasksUseCase: TasksUseCase
) : ViewModel() {

    val allTasks = MutableLiveData<List<TaskViewData>>()
    val completedTasks = MutableLiveData<Int>()
    val tasksVisibility = MutableLiveData<Boolean>()

    private val tasksList = mutableListOf<TaskModel>()
    private val cachedTasks = mutableListOf<TaskModel>()

    init {
        tasksVisibility.value = accountManager.tasksVisibility
        viewModelScope.launch {
            tasksUseCase.getTasksFromCache().collect {
                tasksList.clear()
                tasksList.addAll(it)
                updateUi()
            }
        }
    }

    fun tasksVisibilityClicked() {
        cachedTasks.clear()
        val currentVisibility = accountManager.tasksVisibility
        accountManager.tasksVisibility = !currentVisibility
        tasksVisibility.value = !currentVisibility
        updateUi()
    }

    fun onTaskCheckboxClicked(task: TaskModel) {
        viewModelScope.launch {
            if (task.done) {
                removeTaskFromCache(task)
            } else {
                putTaskInCacheList(task)
            }
            tasksUseCase.updateTask(task.switchIsDone())
        }
    }

    fun taskDoneSwipe(task: TaskModel) {
        viewModelScope.launch {
            if (task.done) {
                removeTaskFromCache(task)
            } else {
                putTaskInCacheList(task)
            }
            tasksUseCase.updateTask(task.switchIsDone())
        }
    }

    fun taskDeleteSwipe(task: TaskModel) {
        viewModelScope.launch {
            tasksUseCase.deleteTask(task)
        }
    }

    private fun removeTaskFromCache(task: TaskModel) {
        cachedTasks.remove(task)
    }

    private fun putTaskInCacheList(task: TaskModel) {
        if (cachedTasks.contains(task.switchIsDone())) return
        cachedTasks.add(task.switchIsDone())
    }

    private fun updateUi() {
        val items = mutableListOf<TaskViewData>()
        items.add(TaskViewData.Header)
        if (accountManager.tasksVisibility) {
            items.addAll(tasksList.sortedWith(TaskComparator()).toViewData())
        } else {
            val filteredTasks = tasksList.filter { !it.done } as MutableList<TaskModel>
            if (cachedTasks.isNotEmpty()) filteredTasks.addAll(cachedTasks)
            items.addAll(filteredTasks.sortedWith(TaskComparator()).toViewData())
        }
        items.add(TaskViewData.NewTask)
        completedTasks.value = tasksList.filter { it.done }.size
        allTasks.value = items
    }
}