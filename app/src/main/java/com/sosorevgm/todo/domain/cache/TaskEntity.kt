package com.sosorevgm.todo.domain.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.TaskPriority
import com.sosorevgm.todo.models.TaskPriorityConstants.TASK_PRIORITY_DEFAULT
import com.sosorevgm.todo.models.TaskPriorityConstants.TASK_PRIORITY_HIGH
import com.sosorevgm.todo.models.TaskPriorityConstants.TASK_PRIORITY_LOW
import com.sosorevgm.todo.models.TaskSynchronizeAction

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "priority")
    val priority: TaskPriority,
    @ColumnInfo(name = "done")
    val done: Boolean,
    @ColumnInfo(name = "deadline")
    val deadline: Long,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
) {
    fun toTaskSynchronizeEntity(action: TaskSynchronizeAction): TaskSynchronizeEntity {
        val importance: String = when (this.priority) {
            TaskPriority.LOW -> TASK_PRIORITY_LOW
            TaskPriority.DEFAULT -> TASK_PRIORITY_DEFAULT
            TaskPriority.HIGH -> TASK_PRIORITY_HIGH
        }
        return TaskSynchronizeEntity(
            action,
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

fun List<TaskEntity>.toTaskModels(): List<TaskModel> {
    val result = mutableListOf<TaskModel>()
    for (nextEntity in this) {
        result.add(
            TaskModel(
                nextEntity.id,
                nextEntity.text,
                nextEntity.priority,
                nextEntity.done,
                nextEntity.deadline,
                nextEntity.createdAt,
                nextEntity.updatedAt
            )
        )
    }
    return result
}