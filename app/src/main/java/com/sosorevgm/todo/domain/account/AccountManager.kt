package com.sosorevgm.todo.domain.account

import android.content.SharedPreferences

class AccountManager(
    private val preferences: SharedPreferences
) {

    companion object {
        private const val TASKS_VISIBILITY = "tasks.visibility"
        private const val AUTHORIZATION_TOKEN = "authorization.token"
        private const val DEFAULT_AUTHORIZATION_TOKEN = "7b65508b61304a87ae125b3d2dbd7497"
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
        return token ?: DEFAULT_AUTHORIZATION_TOKEN
    }

    private fun setAuthToken(token: String) {
        preferences.edit().putString(AUTHORIZATION_TOKEN, token).apply()
    }
}