package com.sosorevgm.todo.di.modules

import com.sosorevgm.todo.BuildConfig
import com.sosorevgm.todo.di.qualifiers.AuthInterceptor
import com.sosorevgm.todo.di.qualifiers.LoggingInterceptor
import com.sosorevgm.todo.di.scopes.AppScope
import com.sosorevgm.todo.domain.account.AccountManager
import com.sosorevgm.todo.domain.api.AuthorizationInterceptor
import com.sosorevgm.todo.domain.api.TasksApi
import com.sosorevgm.todo.domain.network.NetworkResultAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
class RetrofitModule {

    companion object {
        private const val TIMEOUT_REST_SEC: Short = 40
        private const val BASE_URL = "https://d5dps3h13rv6902lp5c8.apigw.yandexcloud.net/"
    }

    @AppScope
    @LoggingInterceptor
    @Provides
    fun providesLoggingInterceptor(): Interceptor = HttpLoggingInterceptor().also {
        it.level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    @AppScope
    @AuthInterceptor
    @Provides
    fun providesAuthorizationInterceptor(accountManager: AccountManager): Interceptor =
        AuthorizationInterceptor(accountManager)

    @AppScope
    @Provides
    fun providesClient(
        @LoggingInterceptor loggingInterceptor: Interceptor,
        @AuthInterceptor tasksInterceptor: Interceptor
    ): OkHttpClient =
        OkHttpClient.Builder()
            .readTimeout(TIMEOUT_REST_SEC.toLong(), TimeUnit.SECONDS)
            .writeTimeout(TIMEOUT_REST_SEC.toLong(), TimeUnit.SECONDS)
            .connectTimeout(TIMEOUT_REST_SEC.toLong(), TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor(tasksInterceptor)
            .build()

    @AppScope
    @Provides
    fun providesTasksApi(
        client: OkHttpClient
    ): TasksApi =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(NetworkResultAdapterFactory())
            .build()
            .create(TasksApi::class.java)
}