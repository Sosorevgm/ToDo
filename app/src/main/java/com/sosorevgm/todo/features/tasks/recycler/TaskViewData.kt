package com.sosorevgm.todo.features.tasks.recycler

import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.TaskPriority

sealed class TaskViewData {
    object Header : TaskViewData()
    data class Task(
        val id: Long,
        val text: String,
        val priority: TaskPriority,
        val done: Boolean,
        val deadline: Long,
        val createdAt: Long,
        val updatedAt: Long
    ) : TaskViewData()

    object NewTask : TaskViewData()
}

fun TaskViewData.Task.toTaskModel() =
    TaskModel(
        this.id,
        this.text,
        this.priority,
        this.done,
        this.deadline,
        this.createdAt,
        this.updatedAt
    )