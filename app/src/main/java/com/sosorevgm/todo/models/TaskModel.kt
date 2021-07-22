
package com.sosorevgm.todo.models

import android.os.Parcelable
import com.sosorevgm.todo.domain.cache.TaskEntity
import com.sosorevgm.todo.domain.cache.TaskSynchronizeEntity
import com.sosorevgm.todo.extensions.getCurrentTimestamp
import com.sosorevgm.todo.extensions.isSameDay
import com.sosorevgm.todo.features.tasks.recycler.TaskViewData
import kotlinx.parcelize.Parcelize
import java.util.*

const val TASK_BUNDLE = "task.bundle"

@Parcelize
data class TaskModel(
    val id: String,
    val text: String,
    val priority: TaskPriority,
    val done: Boolean,
    val deadline: Long,
    val createdAt: Long,
    val updatedAt: Long
) : Parcelable {

    fun switchIsDone() =
        TaskModel(
            this.id,
            this.text,
            this.priority,
            !this.done,
            this.deadline,
            this.createdAt,
            getCurrentTimestamp()
        )

    fun toTaskEntity() =
        TaskEntity(
            this.id,
            this.text,
            this.priority,
            this.done,
            this.deadline,
            this.createdAt,
            this.updatedAt
        )

    fun toTaskSynchronizeEntity(action: TaskSynchronizeAction): TaskSynchronizeEntity =
        TaskSynchronizeEntity(
            action,
            this.id,
            this.text,
            this.priority.toString(),
            this.done,
            this.deadline,
            this.createdAt,
            this.updatedAt
        )

    fun toTaskApiModel(): TaskApiModel = TaskApiModel(
        this.id,
        this.text,
        this.priority.toString(),
        this.done,
        this.deadline,
        this.createdAt,
        this.updatedAt
    )

    fun toTaskApiModelWithId(id: Long): TaskApiModel = TaskApiModel(
        id.toString(),
        this.text,
        this.priority.toString(),
        this.done,
        this.deadline,
        this.createdAt,
        this.updatedAt
    )
}

class TaskComparator : Comparator<TaskModel> {
    override fun compare(taskOne: TaskModel, taskTwo: TaskModel): Int {
        if (taskOne.deadline == 0L && taskTwo.deadline != 0L) {
            return 1
        } else if (taskOne.deadline != 0L && taskTwo.deadline == 0L) {
            return -1
        } else if (taskOne.deadline == 0L && taskTwo.deadline == 0L) {
            return getImportanceToCompare(taskOne, taskTwo)
        }

        if (isSameDay(taskOne.deadline, taskTwo.deadline)) {
            return getImportanceToCompare(taskOne, taskTwo)
        }
        return (taskOne.deadline - taskTwo.deadline).toInt()
    }
}

fun List<TaskModel>.toViewData(): List<TaskViewData.Task> {
    val result = mutableListOf<TaskViewData.Task>()
    for (nextTask in this) {
        result.add(
            TaskViewData.Task(
                nextTask.id,
                nextTask.text,
                nextTask.priority,
                nextTask.done,
                nextTask.deadline,
                nextTask.createdAt,
                nextTask.updatedAt
            )
        )
    }
    return result
}

fun List<TaskModel>.toTaskApiModels(): List<TaskApiModel> {
    val result = mutableListOf<TaskApiModel>()
    for (nextTask in this) {
        result.add(nextTask.toTaskApiModel())
    }
    return result
}

private fun getImportanceToCompare(taskOne: TaskModel, taskTwo: TaskModel): Int {
    if (taskOne.priority == TaskPriority.HIGH && taskTwo.priority != TaskPriority.HIGH) {
        return -1
    } else if (taskOne.priority != TaskPriority.HIGH && taskTwo.priority == TaskPriority.HIGH) {
        return 1
    }
    return 0
}