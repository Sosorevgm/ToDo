package com.sosorevgm.todo.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.sosorevgm.todo.di.scopes.AppScope
import com.sosorevgm.todo.domain.account.AccountManager
import com.sosorevgm.todo.domain.account.AccountManagerImpl
import dagger.Module
import dagger.Provides

@Module
class AccountModule {

    companion object {
        private const val ACCOUNT_PREFERENCES = "account.preferences"
    }

    @AppScope
    @Provides
    fun providesPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(ACCOUNT_PREFERENCES, Context.MODE_PRIVATE)

    @AppScope
    @Provides
    fun providesAccountManager(preferences: SharedPreferences): AccountManager =
        AccountManagerImpl(preferences)
}