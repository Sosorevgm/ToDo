package com.sosorevgm.todo.features.main

import com.sosorevgm.todo.domain.cache.TaskSynchronizeEntity
import com.sosorevgm.todo.models.TaskApiModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SynchronizeTasksUseCase {
    fun getTasksToSynchronize(): Flow<List<TaskSynchronizeEntity>>
    suspend fun checkTasksUpdate()
    suspend fun addTask(task: TaskApiModel)
    suspend fun updateTask(task: TaskApiModel)
    suspend fun deleteTask(task: TaskApiModel)
    suspend fun synchronizeTasks(
        tasksToDelete: List<String>,
        tasksToAdd: List<TaskApiModel>,
        tasksToUpdate: List<TaskApiModel>
    )
}

class SynchronizeTasksUseCaseImpl @Inject constructor(
    private val repository: SynchronizeTasksRepository
) : SynchronizeTasksUseCase {

    override fun getTasksToSynchronize() = repository.getTasksToSynchronize()
    override suspend fun checkTasksUpdate() = repository.checkTasksUpdate()
    override suspend fun addTask(task: TaskApiModel) = repository.addTask(task)
    override suspend fun updateTask(task: TaskApiModel) = repository.updateTask(task)
    override suspend fun deleteTask(task: TaskApiModel) = repository.deleteTask(task)
    override suspend fun synchronizeTasks(
        tasksToDelete: List<String>,
        tasksToAdd: List<TaskApiModel>,
        tasksToUpdate: List<TaskApiModel>
    ) = repository.synchronizeTasks(tasksToDelete, tasksToAdd, tasksToUpdate)
}