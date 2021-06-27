package com.sosorevgm.todo.domain.account

import android.content.SharedPreferences

class AccountManager(
    private val preferences: SharedPreferences
) {

    companion object {
        private const val TASKS_VISIBILITY = "tasks.visibility"
    }

    var tasksVisibility: Boolean
        get() = getTasksDoneVisibility()
        set(value) = setTasksDoneVisibility(value)

    private fun getTasksDoneVisibility(): Boolean = preferences.getBoolean(TASKS_VISIBILITY, false)

    private fun setTasksDoneVisibility(visibility: Boolean) {
        preferences.edit().putBoolean(TASKS_VISIBILITY, visibility).apply()
    }
}