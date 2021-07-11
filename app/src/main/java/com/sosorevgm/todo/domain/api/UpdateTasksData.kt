package com.sosorevgm.todo.domain.api

import com.sosorevgm.todo.models.TaskApiModel

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