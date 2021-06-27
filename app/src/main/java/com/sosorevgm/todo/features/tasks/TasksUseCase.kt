package com.sosorevgm.todo.features.tasks

import com.sosorevgm.todo.models.TaskModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface TasksUseCase {
    fun getTasksFromCache(): Flow<List<TaskModel>>
    suspend fun getAllTasks(): List<TaskModel>
    suspend fun getTasksToDo(): List<TaskModel>
    suspend fun addTask(task: TaskModel)
    suspend fun updateIsDoneTask(task: TaskModel)
    suspend fun updateTask(oldTask: TaskModel, newTask: TaskModel)
    suspend fun deleteTask(task: TaskModel)
}

class TasksUseCaseImpl @Inject constructor(
    private val repository: TasksRepository
) : TasksUseCase {
    override fun getTasksFromCache(): Flow<List<TaskModel>> = repository.getTasksFromCache()

    override suspend fun getAllTasks(): List<TaskModel> = repository.getAllTasks()

    override suspend fun getTasksToDo(): List<TaskModel> = repository.getTasksToDo()

    override suspend fun addTask(task: TaskModel) = repository.addTask(task)

    override suspend fun updateIsDoneTask(task: TaskModel) = repository.updateIsDoneTask(task)

    override suspend fun updateTask(oldTask: TaskModel, newTask: TaskModel) =
        repository.updateTask(oldTask, newTask)

    override suspend fun deleteTask(task: TaskModel) = repository.deleteTask(task)

}