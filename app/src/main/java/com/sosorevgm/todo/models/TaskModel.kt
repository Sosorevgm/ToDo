package com.sosorevgm.todo.models

import android.os.Parcelable
import com.sosorevgm.todo.domain.cache.TaskEntity
import com.sosorevgm.todo.features.tasks.TaskViewData
import kotlinx.parcelize.Parcelize
import java.util.*

const val TASK_BUNDLE = "task.bundle"

@Parcelize
data class TaskModel(
    val description: String,
    val priority: TaskPriority,
    val isDone: Boolean,
    val date: Long
) : Parcelable

enum class TaskPriority {
    LOW, DEFAULT, HIGH
}

class TaskComparator : Comparator<TaskModel> {
    override fun compare(taskOne: TaskModel, taskTwo: TaskModel): Int {
        if (taskOne.date == 0L && taskTwo.date == 0L) {
            return 0
        } else if (taskOne.date == 0L) {
            return 1
        } else if (taskTwo.date == 0L) {
            return -1
        }

        val calendar = GregorianCalendar()
        calendar.timeInMillis = taskOne.date * 1000
        val taskOneYear = calendar.get(Calendar.YEAR)
        val taskOneMonth = calendar.get(Calendar.MONTH)
        val taskOneDay = calendar.get(Calendar.DAY_OF_MONTH)
        calendar.timeInMillis = taskTwo.date * 1000
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
        return 0
    }
}

fun List<TaskModel>.toViewData(): List<TaskViewData.Task> {
    val result = mutableListOf<TaskViewData.Task>()
    for (nextTask in this) {
        result.add(
            TaskViewData.Task(
                nextTask.description,
                nextTask.priority,
                nextTask.isDone,
                nextTask.date
            )
        )
    }
    return result
}

fun TaskModel.switchIsDone() = TaskModel(this.description, this.priority, !this.isDone, this.date)

fun TaskModel.completeTask() = TaskModel(this.description, this.priority, true, this.date)

fun TaskModel.toTaskEntity() = TaskEntity(this.description, this.priority, this.isDone, this.date)

fun TaskPriority.getSpinnerSelection(): Int = when (this) {
    TaskPriority.LOW -> 1
    TaskPriority.DEFAULT -> 0
    TaskPriority.HIGH -> 2
}