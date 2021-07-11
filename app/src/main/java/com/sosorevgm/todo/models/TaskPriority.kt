package com.sosorevgm.todo.models

enum class TaskPriority {
    LOW, DEFAULT, HIGH;

    override fun toString(): String = when (this) {
        LOW -> "low"
        DEFAULT -> "basic"
        HIGH -> "important"
    }

    fun getSpinnerSelection(): Int = when (this) {
        LOW -> 1
        DEFAULT -> 0
        HIGH -> 2
    }
}