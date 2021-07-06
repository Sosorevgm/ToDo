package com.sosorevgm.todo.models

import android.os.Parcelable
import com.sosorevgm.todo.domain.cache.TaskEntity
import com.sosorevgm.todo.features.tasks.recycler.TaskViewData
import kotlinx.parcelize.Parcelize
import java.util.*

const val TASK_BUNDLE = "task.bundle"

@Parcelize
data class TaskModel(
    val id: Long = 0L,
    val text: String,
    val priority: TaskPriority,
    val done: Boolean,
    val deadline: Long,
    val createdAt: Long,
    val updatedAt: Long
) : Parcelable

enum class TaskPriority {
    LOW, DEFAULT, HIGH
}

class TaskComparator : Comparator<TaskModel> {
    override fun compare(taskOne: TaskModel, taskTwo: TaskModel): Int {
        if (taskOne.deadline == 0L && taskTwo.deadline == 0L) {
            return (taskOne.createdAt - taskTwo.createdAt).toInt()
        } else if (taskOne.deadline == 0L) {
            return 1
        } else if (taskTwo.deadline == 0L) {
            return -1
        }

        val calendar = GregorianCalendar()
        calendar.timeInMillis = taskOne.deadline * 1000
        val taskOneYear = calendar.get(Calendar.YEAR)
        val taskOneMonth = calendar.get(Calendar.MONTH)
        val taskOneDay = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.timeInMillis = taskTwo.deadline * 1000
        val taskTwoYear = calendar.get(Calendar.YEAR)
        val taskTwoMonth = calendar.get(Calendar.MONTH)
        val taskTwoDay = calendar.get(Calendar.DAY_OF_MONTH)
        if (taskOneYear < taskTwoYear) return -1
        if (taskOneYear > taskTwoYear) return 1
        if (taskOneMonth < taskTwoMonth) return -1
        if (taskOneMonth > taskTwoMonth) return 1
        if (taskOneDay < taskTwoDay) return -1
        if (taskOneDay > taskTwoDay) return 1
        if (taskOne.priority < taskTwo.priority) return 1
        if (taskOne.priority > taskTwo.priority) return -1

        return (taskOne.createdAt - taskTwo.createdAt).toInt()
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

fun TaskModel.switchIsDone() =
    TaskModel(
        this.id,
        this.text,
        this.priority,
        !this.done,
        this.deadline,
        this.createdAt,
        this.updatedAt
    )

fun TaskModel.toTaskEntity() =
    TaskEntity(
        this.id,
        this.text,
        this.priority,
        this.done,
        this.deadline,
        this.createdAt,
        this.updatedAt
    )

fun TaskPriority.getSpinnerSelection(): Int = when (this) {
    TaskPriority.LOW -> 1
    TaskPriority.DEFAULT -> 0
    TaskPriority.HIGH -> 2
}