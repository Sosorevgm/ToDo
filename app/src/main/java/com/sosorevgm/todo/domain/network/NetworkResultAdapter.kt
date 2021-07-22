package com.sosorevgm.todo.domain.network

import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class NetworkResultAdapter(
    private val type: Type
) : CallAdapter<Type, Call<NetworkResult<Type>>> {

    override fun responseType(): Type = type

    override fun adapt(call: Call<Type>): Call<NetworkResult<Type>> = NetworkResultCall(call)
}