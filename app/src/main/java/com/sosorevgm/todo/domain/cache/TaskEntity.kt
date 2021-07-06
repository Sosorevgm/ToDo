package com.sosorevgm.todo.domain.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.TaskPriority

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
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
)

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