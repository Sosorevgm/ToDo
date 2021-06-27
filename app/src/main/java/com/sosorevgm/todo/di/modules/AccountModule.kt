package com.sosorevgm.todo.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.sosorevgm.todo.domain.account.AccountManager
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AccountModule {

    companion object {
        private const val ACCOUNT_PREFERENCES = "account.preferences"
    }

    @Singleton
    @Provides
    fun providesPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(ACCOUNT_PREFERENCES, Context.MODE_PRIVATE)

    @Singleton
    @Provides
    fun providesAccountManager(preferences: SharedPreferences): AccountManager =
        AccountManager(preferences)
}