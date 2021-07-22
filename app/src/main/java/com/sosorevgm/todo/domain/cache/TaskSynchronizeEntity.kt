package com.sosorevgm.todo.domain.cache

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.sosorevgm.todo.models.TaskApiModel
import com.sosorevgm.todo.models.TaskSynchronizeAction

@Entity(tableName = "tasks_to_synchronize")
data class TaskSynchronizeEntity(
    @ColumnInfo(name = "action")
    val action: TaskSynchronizeAction,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "text")
    val text: String,
    @ColumnInfo(name = "priority")
    val priority: String,
    @ColumnInfo(name = "done")
    val done: Boolean,
    @ColumnInfo(name = "deadline")
    val deadline: Long,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    @ColumnInfo(name = "updated_at")
    val updatedAt: Long
) {

    fun toTaskApiModel(): TaskApiModel = TaskApiModel(
        this.id,
        this.text,
        this.priority,
        this.done,
        this.deadline,
        this.createdAt,
        this.updatedAt
    )
}

fun List<TaskSynchronizeEntity>.toTaskApiModels(): List<TaskApiModel> {
    val result = mutableListOf<TaskApiModel>()
    for (nextTask in this) {
        result.add(nextTask.toTaskApiModel())
    }
    return result
}