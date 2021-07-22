package com.sosorevgm.todo.domain.api

import com.sosorevgm.todo.models.TaskApiModel

/**
 * This data class is required to synchronization request to the server.
 * UpdateTasksData contains a list of task id's to delete and a list of tasks to add or update
 */

data class UpdateTasksData(
    val deleted: List<String>,
    val other: List<TaskApiModel>
) {

    fun getDataIds(): Array<String> {
        val result = mutableListOf<String>()
        result.addAll(this.deleted)
        for (nextTask in other) {
            result.add(nextTask.id)
        }
        return result.toTypedArray()
    }
}