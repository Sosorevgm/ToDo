package com.sosorevgm.todo.features.main

import com.sosorevgm.todo.domain.api.TasksApi
import com.sosorevgm.todo.domain.api.UpdateTasksData
import com.sosorevgm.todo.domain.cache.TaskEntity
import com.sosorevgm.todo.domain.cache.TaskSynchronizeEntity
import com.sosorevgm.todo.domain.cache.TasksDao
import com.sosorevgm.todo.domain.cache.TasksToSynchronizeDao
import com.sosorevgm.todo.domain.network.fold
import com.sosorevgm.todo.models.TaskApiModel
import com.sosorevgm.todo.models.toTaskEntities
import kotlinx.coroutines.flow.Flow
import timber.log.Timber
import javax.inject.Inject

interface SynchronizeTasksRepository {
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

class SynchronizeTasksRepositoryImpl @Inject constructor(
    private val tasksDao: TasksDao,
    private val tasksToSynchronizeDao: TasksToSynchronizeDao,
    private val api: TasksApi

) : SynchronizeTasksRepository {
    override fun getTasksToSynchronize(): Flow<List<TaskSynchronizeEntity>> =
        tasksToSynchronizeDao.getTasksToSynchronize()

    override suspend fun checkTasksUpdate() {
        api.getAllTasks().fold(onSuccess = { apiModels ->
            val tasksFromServer = apiModels.toTaskEntities()
            val tasksFromDatabase = tasksDao.getAllTasks()
            val tasksToSynchronizeToDelete = tasksToSynchronizeDao.getTasksToDelete()
            val tasksToSynchronizeToAdd = tasksToSynchronizeDao.getTasksToAdd()

            val tasksToAdd = mutableListOf<TaskEntity>()
            val tasksToUpdate = mutableListOf<TaskEntity>()
            val tasksToDelete = mutableListOf<TaskEntity>()

            for (serverTask in tasksFromServer) {
                val databaseTask = tasksFromDatabase.firstOrNull { it.id == serverTask.id }
                val synchronizedTaskToDelete =
                    tasksToSynchronizeToDelete.firstOrNull { it.id == serverTask.id }
                if (databaseTask == null) {
                    // if the main table and sync table do not contain the server task,
                    // add it to the main table
                    if (synchronizedTaskToDelete == null) {
                        tasksToAdd.add(serverTask)
                    }
                } else {
                    // if the update time of the database task happened earlier
                    // than the server update task in local database
                    if (serverTask != databaseTask && serverTask.updatedAt > databaseTask.updatedAt) {
                        tasksToUpdate.add(serverTask)
                    }
                }
            }

            for (databaseTask in tasksFromDatabase) {
                // check the sync table before deleting the task from the main table
                val serverTask = tasksFromServer.firstOrNull { it.id == databaseTask.id }
                val taskToAdd = tasksToSynchronizeToAdd.firstOrNull { it.id == databaseTask.id }
                if (serverTask == null && taskToAdd == null) {
                    tasksToDelete.add(databaseTask)
                }
            }

            tasksDao.synchronizeTasks(tasksToAdd, tasksToUpdate, tasksToDelete)
        }, onFailure = { code, err ->
            Timber.e("Get all tasks error code: $code. Error: $err")
        })
    }

    override suspend fun addTask(task: TaskApiModel) {
        api.addTask(task).fold(onSuccess = {
            tasksToSynchronizeDao.deleteSingleTaskById(task.id)
        }, onFailure = { code, err ->
            Timber.e("Add task error code: $code. Error: $err")
        })
    }

    override suspend fun updateTask(task: TaskApiModel) {
        api.updateTask(task.id, task).fold(onSuccess = {
            tasksToSynchronizeDao.deleteSingleTaskById(task.id)
        }, onFailure = { code, err ->
            Timber.e("Update task error code: $code. Error: $err")
        })
    }

    override suspend fun deleteTask(task: TaskApiModel) {
        api.deleteTask(task.id).fold(onSuccess = {
            tasksToSynchronizeDao.deleteSingleTaskById(task.id)
        }, onFailure = { code, err ->
            Timber.e("Delete task error code: $code. Error: $err")
        })
    }

    override suspend fun synchronizeTasks(
        tasksToDelete: List<String>,
        tasksToAdd: List<TaskApiModel>,
        tasksToUpdate: List<TaskApiModel>
    ) {
        val tasksToAddAndUpdate = mutableListOf<TaskApiModel>().apply {
            addAll(tasksToAdd)
            addAll(tasksToUpdate)
        }
        val data = UpdateTasksData(tasksToDelete, tasksToAddAndUpdate)
        api.synchronizeTasks(data).fold(onSuccess = {
            tasksToSynchronizeDao.deleteTasksByIds(data.getDataIds())
        }, onFailure = { code, err ->
            Timber.e("Synchronize tasks error code: $code. Error: $err")
        })
    }
}