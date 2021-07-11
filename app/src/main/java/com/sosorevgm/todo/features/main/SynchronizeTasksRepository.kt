package com.sosorevgm.todo.features.main

import com.sosorevgm.todo.domain.api.TasksApi
import com.sosorevgm.todo.domain.api.UpdateTasksData
import com.sosorevgm.todo.domain.cache.TaskEntity
import com.sosorevgm.todo.domain.cache.TaskSynchronizeEntity
import com.sosorevgm.todo.domain.cache.TasksDao
import com.sosorevgm.todo.domain.cache.TasksToSynchronizeDao
import com.sosorevgm.todo.domain.network.fold
import com.sosorevgm.todo.models.TaskApiModel
import com.sosorevgm.todo.models.TaskSynchronizeAction
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
                // check the synchronization table before adding the task to the main table
                if (databaseTask == null && tasksToSynchronizeToDelete.firstOrNull { it.id == serverTask.id } == null) {
                    tasksToAdd.add(serverTask)
                    // if the update time of the database task happened earlier
                    // than the server update task in local database
                } else if (serverTask != databaseTask && serverTask.updatedAt > databaseTask!!.updatedAt) {
                    tasksToUpdate.add(serverTask)
                    // if the update time of the server task happened earlier
                    // than the database task send this task to the server
                } else if (serverTask != databaseTask && serverTask.updatedAt < databaseTask.updatedAt) {
                    tasksToSynchronizeDao.insertTask(
                        databaseTask.toTaskSynchronizeEntity(
                            TaskSynchronizeAction.UPDATE
                        )
                    )
                }
            }

            for (databaseTask in tasksFromDatabase) {
                // check the synchronization table before deleting the task from the main table
                if (tasksFromServer.firstOrNull { it.id == databaseTask.id } == null && tasksToSynchronizeToAdd.firstOrNull { it.id == databaseTask.id } == null) {
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