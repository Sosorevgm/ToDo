package com.sosorevgm.todo.models

import com.google.gson.annotations.SerializedName
import com.sosorevgm.todo.domain.cache.TaskEntity
import com.sosorevgm.todo.models.TaskPriorityConstants.TASK_PRIORITY_DEFAULT
import com.sosorevgm.todo.models.TaskPriorityConstants.TASK_PRIORITY_HIGH
import com.sosorevgm.todo.models.TaskPriorityConstants.TASK_PRIORITY_LOW


data class TaskApiModel(
    val id: String,
    val text: String,
    val importance: String,
    val done: Boolean,
    val deadline: Long,
    @SerializedName("created_at") val createdAt: Long,
    @SerializedName("updated_at") val updatedAt: Long
) {
    fun toTaskModel(): TaskModel {
        val importance: TaskPriority = when (this.importance) {
            TASK_PRIORITY_LOW -> TaskPriority.LOW
            TASK_PRIORITY_DEFAULT -> TaskPriority.DEFAULT
            TASK_PRIORITY_HIGH -> TaskPriority.HIGH
            else -> throw Exception("Wrong priority type: ${this.importance}")
        }
        return TaskModel(
            this.id,
            this.text,
            importance,
            this.done,
            this.deadline,
            this.createdAt,
            this.updatedAt
        )
    }

    fun toTaskEntity(): TaskEntity {
        val importance: TaskPriority = when (this.importance) {
            TASK_PRIORITY_LOW -> TaskPriority.LOW
            TASK_PRIORITY_DEFAULT -> TaskPriority.DEFAULT
            TASK_PRIORITY_HIGH -> TaskPriority.HIGH
            else -> throw Exception("Wrong priority type: ${this.importance}")
        }
        return TaskEntity(
            this.id,
            this.text,
            importance,
            this.done,
            this.deadline,
            this.createdAt,
            this.updatedAt
        )
    }
}

fun List<TaskApiModel>.toTaskModels(): List<TaskModel> {
    val result = mutableListOf<TaskModel>()
    for (nextTaskApiModel in this) {
        result.add(nextTaskApiModel.toTaskModel())
    }
    return result
}

fun List<TaskApiModel>.toTaskEntities(): List<TaskEntity> {
    val result = mutableListOf<TaskEntity>()
    for (nextTaskApiModel in this) {
        result.add(nextTaskApiModel.toTaskEntity())
    }
    return result
}