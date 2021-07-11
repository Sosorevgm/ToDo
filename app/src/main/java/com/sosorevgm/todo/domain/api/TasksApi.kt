package com.sosorevgm.todo.domain.api

import com.sosorevgm.todo.domain.network.NetworkResult
import com.sosorevgm.todo.models.TaskApiModel
import retrofit2.http.*

interface TasksApi {

    @GET("tasks")
    suspend fun getAllTasks(): NetworkResult<List<TaskApiModel>>

    @POST("tasks")
    suspend fun addTask(
        @Body task: TaskApiModel
    ): NetworkResult<Unit>

    @PUT("tasks/{taskId}")
    suspend fun updateTask(
        @Path("taskId") taskId: String,
        @Body task: TaskApiModel
    ): NetworkResult<Unit>

    @DELETE("tasks/{taskId}")
    suspend fun deleteTask(
        @Path("taskId") taskId: String
    ): NetworkResult<Unit>

    @PUT("tasks")
    suspend fun synchronizeTasks(
        @Body data: UpdateTasksData
    ): NetworkResult<List<TaskApiModel>>
}