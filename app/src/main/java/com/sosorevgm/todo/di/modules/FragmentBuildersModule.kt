package com.sosorevgm.todo.di.modules

import com.sosorevgm.todo.di.scopes.AppScope
import com.sosorevgm.todo.features.new_task.NewTaskFragment
import com.sosorevgm.todo.features.tasks.TasksFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeTasksFragment(): TasksFragment

    @ContributesAndroidInjector
    abstract fun contributeNewTaskFragment(): NewTaskFragment
}