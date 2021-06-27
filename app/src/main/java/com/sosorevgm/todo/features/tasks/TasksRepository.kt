package com.sosorevgm.todo.features.tasks

import com.sosorevgm.todo.domain.cache.TasksDao
import com.sosorevgm.todo.domain.cache.toTaskModels
import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.toTaskEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface TasksRepository {
    fun getTasksFromCache(): Flow<List<TaskModel>>
    suspend fun getAllTasks(): List<TaskModel>
    suspend fun getTasksToDo(): List<TaskModel>
    suspend fun addTask(task: TaskModel)
    suspend fun updateIsDoneTask(task: TaskModel)
    suspend fun updateTask(oldTask: TaskModel, newTask: TaskModel)
    suspend fun deleteTask(task: TaskModel)
}

class TasksRepositoryImpl @Inject constructor(
    private val cache: TasksDao
) : TasksRepository {
    override fun getTasksFromCache(): Flow<List<TaskModel>> =
        cache.getTasksFlow().map { it.toTaskModels() }

    override suspend fun getAllTasks(): List<TaskModel> = cache.getTasks().toTaskModels()

    override suspend fun getTasksToDo(): List<TaskModel> = cache.getTasksToDo().toTaskModels()

    override suspend fun addTask(task: TaskModel) = cache.insertTask(task.toTaskEntity())

    override suspend fun updateIsDoneTask(task: TaskModel) =
        cache.updateIsDoneTask(task.toTaskEntity())

    override suspend fun updateTask(oldTask: TaskModel, newTask: TaskModel) =
        cache.updateTask(oldTask.toTaskEntity(), newTask.toTaskEntity())

    override suspend fun deleteTask(task: TaskModel) = cache.deleteTask(task.toTaskEntity())
}