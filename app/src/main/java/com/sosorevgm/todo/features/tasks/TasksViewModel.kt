package com.sosorevgm.todo.features.tasks

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sosorevgm.todo.domain.account.AccountManager
import com.sosorevgm.todo.domain.navigation.Navigation
import com.sosorevgm.todo.domain.presentation.SingleLiveEvent
import com.sosorevgm.todo.features.tasks.recycler.TaskViewData
import com.sosorevgm.todo.models.TaskComparator
import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.toViewData
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    private val accountManager: AccountManager,
    private val tasksUseCase: TasksUseCase
) : ViewModel() {

    val tasks = MutableLiveData<List<TaskViewData>>()
    val completedTasks = MutableLiveData<Int>()
    val tasksVisibility = MutableLiveData<Boolean>()
    val navigation = SingleLiveEvent<Navigation.Event>()

    private val tasksList = mutableListOf<TaskModel>()

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

    fun tasksVisibilityClick() {
        val currentVisibility = accountManager.tasksVisibility
        accountManager.tasksVisibility = !currentVisibility
        tasksVisibility.value = !currentVisibility
        updateUi()
    }

    fun onTaskCheckboxClick(task: TaskModel) {
        viewModelScope.launch {
            tasksUseCase.updateTask(task.switchIsDone())
        }
    }

    fun taskDoneSwipe(task: TaskModel) {
        viewModelScope.launch {
            tasksUseCase.updateTask(task.switchIsDone())
        }
    }

    fun taskDeleteSwipe(task: TaskModel) {
        viewModelScope.launch {
            tasksUseCase.deleteTask(task)
        }
    }

    fun onTaskClick(task: TaskModel) {
        navigation.value = Navigation.getEvent(Navigation.Screen.NEW_TASK, task)
    }

    fun onNewTaskClick() {
        navigation.value = Navigation.getEvent(Navigation.Screen.NEW_TASK, null)
    }

    private fun updateUi() {
        val items = mutableListOf<TaskViewData>()
        items.add(TaskViewData.Header)
        if (accountManager.tasksVisibility) {
            items.addAll(tasksList.sortedWith(TaskComparator()).toViewData())
        } else {
            val filteredTasks = tasksList.filter { !it.done } as MutableList<TaskModel>
            items.addAll(filteredTasks.sortedWith(TaskComparator()).toViewData())
        }
        items.add(TaskViewData.NewTask)
        completedTasks.value = tasksList.filter { it.done }.size
        tasks.value = items
    }
}