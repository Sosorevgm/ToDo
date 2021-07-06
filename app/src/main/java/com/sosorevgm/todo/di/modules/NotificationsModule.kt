package com.sosorevgm.todo.di.modules

import android.content.Context
import com.sosorevgm.todo.di.scopes.AppScope
import com.sosorevgm.todo.domain.notifications.PushManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class NotificationsModule {

    @AppScope
    @Provides
    fun providesPushManager(context: Context): PushManager = PushManager(context)
}