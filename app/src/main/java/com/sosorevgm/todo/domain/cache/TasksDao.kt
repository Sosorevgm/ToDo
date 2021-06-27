package com.sosorevgm.todo.domain.cache

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksDao {

    @Query("SELECT * FROM tasks")
    fun getTasksFlow(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks")
    suspend fun getTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE is_done = 0")
    suspend fun getTasksToDo(): List<TaskEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateIsDoneTask(task: TaskEntity)

    @Transaction
    suspend fun updateTask(oldTask: TaskEntity, newTask: TaskEntity) {
        deleteTask(oldTask)
        insertTask(newTask)
    }

    @Delete
    suspend fun deleteTask(task: TaskEntity)
}