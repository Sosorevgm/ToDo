package com.sosorevgm.todo.features.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sosorevgm.todo.domain.account.AccountManager
import com.sosorevgm.todo.domain.navigation.Navigation
import com.sosorevgm.todo.features.tasks.recycler.TaskViewData
import com.sosorevgm.todo.models.TaskComparator
import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.toViewData
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

class TasksViewModel @Inject constructor(
    private val accountManager: AccountManager,
    private val tasksUseCase: TasksUseCase
) : ViewModel() {

    private val _tasksList = mutableListOf<TaskModel>()
    private val _tasks = MutableStateFlow(emptyList<TaskViewData>())
    private val _completedTasks = MutableStateFlow(0)
    private val _tasksVisibility = MutableStateFlow(false)
    private val _navigation = MutableSharedFlow<Navigation.Event>()
    val tasks: StateFlow<List<TaskViewData>> = _tasks.asStateFlow()
    val completedTasks: StateFlow<Int> = _completedTasks.asStateFlow()
    val tasksVisibility: StateFlow<Boolean> = _tasksVisibility.asStateFlow()
    val navigation: SharedFlow<Navigation.Event> = _navigation.asSharedFlow()

    init {
        _tasksVisibility.value = accountManager.tasksVisibility
        viewModelScope.launch {
            tasksUseCase.getTasksFromCache().collect {
                _tasksList.clear()
                _tasksList.addAll(it)
                updateUi()
            }
        }
    }

    fun tasksVisibilityClick() {
        val currentVisibility = accountManager.tasksVisibility
        accountManager.tasksVisibility = !currentVisibility
        _tasksVisibility.value = !currentVisibility
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
        viewModelScope.launch {
            _navigation.emit(Navigation.getEvent(Navigation.Screen.NEW_TASK, task))
        }
    }

    fun onNewTaskClick() {
        viewModelScope.launch {
            _navigation.emit(Navigation.getEvent(Navigation.Screen.NEW_TASK, null))
        }
    }

    private fun updateUi() {
        val items = mutableListOf<TaskViewData>()
        items.add(TaskViewData.Header)
        if (accountManager.tasksVisibility) {
            items.addAll(_tasksList.sortedWith(TaskComparator()).toViewData())
        } else {
            val filteredTasks = _tasksList.filter { !it.done } as MutableList<TaskModel>
            items.addAll(filteredTasks.sortedWith(TaskComparator()).toViewData())
        }
        items.add(TaskViewData.NewTask)
        _completedTasks.value = _tasksList.filter { it.done }.size
        _tasks.value = items
    }
}