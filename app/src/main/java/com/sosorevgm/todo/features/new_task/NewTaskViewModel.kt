package com.sosorevgm.todo.features.new_task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sosorevgm.todo.extensions.call
import com.sosorevgm.todo.extensions.getCurrentTimestamp
import com.sosorevgm.todo.extensions.getTimestamp
import com.sosorevgm.todo.features.tasks.TasksUseCase
import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.TaskPriority
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

class NewTaskViewModel @Inject constructor(
    private val tasksUseCase: TasksUseCase
) : ViewModel() {

    private val _dateFlow = MutableStateFlow(0L)
    private val _switchEvent = MutableSharedFlow<Boolean>()
    private val _navigationBack = MutableSharedFlow<Unit>()
    val dateFlow: StateFlow<Long> = _dateFlow.asStateFlow()
    val switchEvent: SharedFlow<Boolean> = _switchEvent.asSharedFlow()
    val navigationBack: SharedFlow<Unit> = _navigationBack.asSharedFlow()

    var oldTask: TaskModel? = null
    private var priority = TaskPriority.DEFAULT
    private var date = 0L

    fun btnSaveClicked(description: String) {
        viewModelScope.launch {
            if (oldTask == null) {
                tasksUseCase.addTask(getNewTask(description))
            } else {
                tasksUseCase.updateTask(getUpdatedTask(description))
            }
            _navigationBack.call()
        }
    }

    fun setPriority(taskPriority: TaskPriority) {
        priority = taskPriority
    }

    fun setDate(year: Int, month: Int, day: Int) {
        val timeStamp = getTimestamp(year, month, day)
        date = timeStamp
        _dateFlow.value = timeStamp
    }

    fun switchClicked() {
        viewModelScope.launch {
            if (date == 0L) {
                _switchEvent.emit(true)
            } else {
                date = 0L
                _dateFlow.value = date
                _switchEvent.emit(false)
            }
        }
    }

    fun setDate(taskDate: Long) {
        date = taskDate
    }

    fun deleteOldTask() {
        viewModelScope.launch {
            oldTask?.let {
                tasksUseCase.deleteTask(it)
            }
            _navigationBack.call()
        }
    }

    private fun getNewTask(description: String): TaskModel = TaskModel(
        UUID.randomUUID().toString(),
        description,
        priority,
        false,
        date,
        getCurrentTimestamp(),
        0
    )

    private fun getUpdatedTask(description: String): TaskModel = TaskModel(
        oldTask!!.id,
        description,
        priority,
        oldTask!!.done,
        date,
        oldTask!!.createdAt,
        getCurrentTimestamp()
    )
}