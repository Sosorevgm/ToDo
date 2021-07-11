package com.sosorevgm.todo.domain.cache

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Query("SELECT * FROM tasks")
    fun getTasksFlow(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE done = 0")
    suspend fun getTasksToDo(): List<TaskEntity>

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Insert
    suspend fun insertTasks(tasks: List<TaskEntity>)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Update
    suspend fun updateTasks(tasks: List<TaskEntity>)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Delete
    suspend fun deleteTasks(tasks: List<TaskEntity>)

    @Transaction
    suspend fun synchronizeTasks(
        tasksToAdd: List<TaskEntity>,
        tasksToUpdate: List<TaskEntity>,
        tasksToDelete: List<TaskEntity>
    ) {
        insertTasks(tasksToAdd)
        updateTasks(tasksToUpdate)
        deleteTasks(tasksToDelete)
    }
}