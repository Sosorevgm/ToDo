package com.sosorevgm.todo.domain.account

import android.content.SharedPreferences
import com.sosorevgm.todo.BuildConfig

class AccountManager(
    private val preferences: SharedPreferences
) {

    companion object {
        private const val TASKS_VISIBILITY = "tasks.visibility"
        private const val AUTHORIZATION_TOKEN = "authorization.token"
    }

    var tasksVisibility: Boolean
        get() = getTasksDoneVisibility()
        set(value) = setTasksDoneVisibility(value)

    var authorizationToken: String
        get() = getAuthToken()
        set(value) = setAuthToken(value)

    private fun getTasksDoneVisibility(): Boolean = preferences.getBoolean(TASKS_VISIBILITY, false)

    private fun setTasksDoneVisibility(visibility: Boolean) {
        preferences.edit().putBoolean(TASKS_VISIBILITY, visibility).apply()
    }

    private fun getAuthToken(): String {
        val token = preferences.getString(AUTHORIZATION_TOKEN, null)
        return token ?: BuildConfig.DEFAULT_AUTHORIZATION_TOKEN
    }

    private fun setAuthToken(token: String) {
        preferences.edit().putString(AUTHORIZATION_TOKEN, token).apply()
    }
}