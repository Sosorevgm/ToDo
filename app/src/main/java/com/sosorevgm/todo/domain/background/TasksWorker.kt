package com.sosorevgm.todo.domain.background

import android.content.Context
import android.text.format.DateUtils
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sosorevgm.todo.domain.notifications.PushManager
import com.sosorevgm.todo.features.tasks.TasksUseCase
import java.util.*

class TasksWorker(
    appContext: Context,
    workerParameters: WorkerParameters,
    private val tasksUseCase: TasksUseCase,
    private val pushManager: PushManager
) : CoroutineWorker(appContext, workerParameters) {

    companion object {
        private const val NINE_OCLOCK = 9
    }

    override suspend fun doWork(): Result {
        val currentHour = GregorianCalendar().get(Calendar.HOUR_OF_DAY)
        if (currentHour == NINE_OCLOCK) {
            var tasksCount = 0
            val tasks = tasksUseCase.getTasksToDo()
            for (nextTask in tasks) {
                if (DateUtils.isToday(nextTask.date * 1000)) {
                    tasksCount++
                }
            }
            return if (tasksCount != 0) {
                pushManager.showNotification(tasksCount)
                Result.retry()
            } else {
                Result.retry()
            }
        } else {
            return Result.retry()
        }
    }
}