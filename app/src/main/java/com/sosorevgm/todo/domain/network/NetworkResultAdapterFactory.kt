package com.sosorevgm.todo.domain.network

import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class NetworkResultAdapterFactory : CallAdapter.Factory() {

    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ) = when (getRawType(returnType)) {
        Call::class.java -> handleCallType(returnType)
        else -> null
    }

    private fun handleCallType(
        returnType: Type,
    ): CallAdapter<*, *>? {
        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(callType) != NetworkResult::class.java) return null

        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
        return NetworkResultAdapter(resultType)
    }
}