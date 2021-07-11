package com.sosorevgm.todo.features.tasks

import com.sosorevgm.todo.domain.cache.TasksDao
import com.sosorevgm.todo.domain.cache.TasksToSynchronizeDao
import com.sosorevgm.todo.domain.cache.toTaskModels
import com.sosorevgm.todo.models.TaskModel
import com.sosorevgm.todo.models.TaskSynchronizeAction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

interface TasksRepository {
    fun getTasksFromCache(): Flow<List<TaskModel>>
    suspend fun getTasksToDo(): List<TaskModel>
    suspend fun addTask(task: TaskModel)
    suspend fun updateTask(task: TaskModel)
    suspend fun deleteTask(task: TaskModel)
}

class TasksRepositoryImpl @Inject constructor(
    private val tasksDao: TasksDao,
    private val tasksToSynchronizeDao: TasksToSynchronizeDao
) : TasksRepository {
    override fun getTasksFromCache(): Flow<List<TaskModel>> =
        tasksDao.getTasksFlow().map { it.toTaskModels() }

    override suspend fun getTasksToDo(): List<TaskModel> = tasksDao.getTasksToDo().toTaskModels()

    override suspend fun addTask(task: TaskModel) {
        tasksDao.insertTask(task.toTaskEntity())
        tasksToSynchronizeDao.insertTask(task.toTaskSynchronizeEntity(TaskSynchronizeAction.ADD))
    }

    override suspend fun updateTask(task: TaskModel) {
        tasksDao.updateTask(task.toTaskEntity())
        tasksToSynchronizeDao.insertTask(task.toTaskSynchronizeEntity(TaskSynchronizeAction.UPDATE))
    }

    override suspend fun deleteTask(task: TaskModel) {
        tasksDao.deleteTask(task.toTaskEntity())
        tasksToSynchronizeDao.insertTask(task.toTaskSynchronizeEntity(TaskSynchronizeAction.DELETE))
    }
}