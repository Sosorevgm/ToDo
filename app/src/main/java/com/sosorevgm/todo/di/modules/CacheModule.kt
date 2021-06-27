package com.sosorevgm.todo.di.modules

import android.content.Context
import androidx.room.Room
import com.sosorevgm.todo.domain.cache.TasksDao
import com.sosorevgm.todo.domain.cache.TasksDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CacheModule {

    companion object {
        private const val DATABASE_NAME = "todo.cache.database"
    }

    @Singleton
    @Provides
    fun providesTasksDao(context: Context): TasksDao = Room.databaseBuilder(
        context,
        TasksDatabase::class.java,
        DATABASE_NAME
    ).build().tasksDao
}