package com.sosorevgm.todo.domain.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.TaskPriority

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey
    @ColumnInfo(name = "description")
    val description: String,
    @ColumnInfo(name = "priority")
    val priority: TaskPriority,
    @ColumnInfo(name = "is_done")
    val isDone: Boolean,
    @ColumnInfo(name = "date")
    val date: Long
)

fun List<TaskEntity>.toTaskModels(): List<TaskModel> {
    val result = mutableListOf<TaskModel>()
    for (nextEntity in this) {
        result.add(
            TaskModel(
                nextEntity.description,
                nextEntity.priority,
                nextEntity.isDone,
                nextEntity.date
            )
        )
    }
    return result
}