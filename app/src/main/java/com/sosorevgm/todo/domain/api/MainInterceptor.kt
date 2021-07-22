package com.sosorevgm.todo.domain.api

import com.sosorevgm.todo.domain.account.AccountManager
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class MainInterceptor @Inject constructor(
    private val accountManager: AccountManager
) : Interceptor {

    companion object {
        private const val AUTHORIZATION = "Authorization"
        private const val BEARER = "Bearer"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(AUTHORIZATION, "$BEARER ${accountManager.authorizationToken}").build()
        return chain.proceed(request)
    }
}