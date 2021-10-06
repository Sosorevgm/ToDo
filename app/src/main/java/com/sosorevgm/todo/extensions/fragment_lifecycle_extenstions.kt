package com.sosorevgm.todo.extensions

import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job

fun Fragment.launchWhenStarted(block: suspend CoroutineScope.() -> Unit): Job {
    return this.viewLifecycleOwner.lifecycleScope.launchWhenStarted(block)
}