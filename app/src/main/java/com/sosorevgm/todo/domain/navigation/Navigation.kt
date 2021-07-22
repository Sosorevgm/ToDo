package com.sosorevgm.todo.domain.navigation

import com.sosorevgm.todo.models.TaskModel

abstract class Navigation {
    companion object {
        fun getEvent(screen: Screen, task: TaskModel?): Event {
            return Event(screen, task)
        }
    }

    data class Event(
        val screen: Screen,
        val task: TaskModel?
    )

    enum class Screen {
        NEW_TASK
    }
}