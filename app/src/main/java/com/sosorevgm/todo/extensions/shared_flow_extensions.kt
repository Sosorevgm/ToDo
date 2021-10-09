package com.sosorevgm.todo.extensions

import kotlinx.coroutines.flow.MutableSharedFlow

suspend fun MutableSharedFlow<Unit>.call() {
    this.emit(Unit)
}