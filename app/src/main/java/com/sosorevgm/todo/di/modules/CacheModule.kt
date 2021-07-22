package com.sosorevgm.todo.di.modules

import android.content.Context
import androidx.room.Room
import com.sosorevgm.todo.di.scopes.AppScope
import com.sosorevgm.todo.domain.cache.TasksDao
import com.sosorevgm.todo.domain.cache.TasksDatabase
import com.sosorevgm.todo.domain.cache.TasksToSynchronizeDao
import dagger.Module
import dagger.Provides

@Module
class CacheModule {

    companion object {
        private const val DATABASE_NAME = "todo.cache.database"
    }

    @AppScope
    @Provides
    fun providesDatabase(context: Context): TasksDatabase = Room.databaseBuilder(
        context,
        TasksDatabase::class.java, DATABASE_NAME
    ).build()

    @AppScope
    @Provides
    fun providesTasksDao(database: TasksDatabase): TasksDao = database.tasksDao

    @AppScope
    @Provides
    fun providesTasksToSynchronizeDao(database: TasksDatabase): TasksToSynchronizeDao =
        database.tasksToSynchronizeDao
}