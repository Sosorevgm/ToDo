package com.sosorevgm.todo.features.new_task

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sosorevgm.todo.domain.presentation.SingleLiveEvent
import com.sosorevgm.todo.extensions.getTimestamp
import com.sosorevgm.todo.features.tasks.TasksUseCase
import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.TaskPriority
import kotlinx.coroutines.launch
import javax.inject.Inject

class NewTaskViewModel @Inject constructor(
    private val tasksUseCase: TasksUseCase
) : ViewModel() {

    val dateLiveData: MutableLiveData<Long> by lazy {
        MutableLiveData<Long>()
    }
    val switchEvent = SingleLiveEvent<Boolean>()
    val navigationBack = SingleLiveEvent<Void>()

    private var oldTask: TaskModel? = null
    private var priority = TaskPriority.DEFAULT
    private var date = 0L

    fun btnSaveClicked(description: String) {
        viewModelScope.launch {
            val newTask = TaskModel(description, priority, false, date)
            if (oldTask == null) {
                tasksUseCase.addTask(newTask)
                navigationBack.call()
            } else {
                if (oldTask != newTask) {
                    tasksUseCase.updateTask(oldTask!!, newTask)
                    navigationBack.call()
                }
            }
        }
    }

    fun setOldTask(task: TaskModel) {
        oldTask = task
    }

    fun setPriority(taskPriority: TaskPriority) {
        priority = taskPriority
    }

    fun setDate(year: Int, month: Int, day: Int) {
        val timeStamp = getTimestamp(year, month, day)
        date = timeStamp
        dateLiveData.value = timeStamp
    }

    fun switchClicked() {
        if (date == 0L) {
            switchEvent.value = true
        } else {
            date = 0L
            dateLiveData.value = date
            switchEvent.value = false
        }
    }

    fun setDate(taskDate: Long) {
        date = taskDate
    }

    fun deleteOldTask() {
        viewModelScope.launch {
            oldTask?.let {
                tasksUseCase.deleteTask(it)
                navigationBack.call()
            }
        }
    }

}