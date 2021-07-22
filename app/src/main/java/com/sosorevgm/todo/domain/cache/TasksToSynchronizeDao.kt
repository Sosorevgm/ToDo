package com.sosorevgm.todo.domain.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.sosorevgm.todo.models.TaskSynchronizeAction
import kotlinx.coroutines.flow.Flow

@Dao
interface TasksToSynchronizeDao {

    @Query("SELECT * FROM tasks_to_synchronize")
    fun getTasksToSynchronize(): Flow<List<TaskSynchronizeEntity>>

    @Query("SELECT * FROM tasks_to_synchronize WHERE `action`=:deleteAction")
    suspend fun getTasksToDelete(deleteAction: TaskSynchronizeAction = TaskSynchronizeAction.DELETE): List<TaskSynchronizeEntity>

    @Query("SELECT * FROM tasks_to_synchronize WHERE `action`=:addAction")
    suspend fun getTasksToAdd(addAction: TaskSynchronizeAction = TaskSynchronizeAction.ADD): List<TaskSynchronizeEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskSynchronizeEntity)

    @Query("DELETE FROM tasks_to_synchronize WHERE id =:taskId")
    suspend fun deleteSingleTaskById(taskId: String)

    @Query("DELETE FROM tasks_to_synchronize WHERE id IN (:ids)")
    suspend fun deleteTasksByIds(ids: Array<String>)
}