package com.sosorevgm.todo.features.tasks

import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.TaskPriority

sealed class TaskViewData {
    object Header : TaskViewData()
    data class Task(
        val description: String,
        val priority: TaskPriority,
        val isDone: Boolean,
        val date: Long
    ) : TaskViewData()

    object NewTask : TaskViewData()
}

fun TaskViewData.Task.toTaskModel() =
    TaskModel(this.description, this.priority, this.isDone, this.date)