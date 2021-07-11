package com.sosorevgm.todo.domain.network

import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class NetworkResultCall<T>(proxy: Call<T>) : CallDelegate<T, NetworkResult<T>>(proxy) {

    override fun enqueueImpl(callback: Callback<NetworkResult<T>>) =
        proxy.enqueue(object : Callback<T> {

            override fun onResponse(call: Call<T>, response: Response<T>) {
                val code = response.code()
                val body = response.body()
                val result = if (code in 200 until 300 && body != null) {
                    NetworkResult.Success(body)
                } else {
                    val error = response.errorBody()
                    NetworkResult.Failure(code, error.toString())
                }

                callback.onResponse(this@NetworkResultCall, Response.success(result))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                val result = if (t is IOException) {
                    NetworkResult.Failure(null, "IOException: $t")
                } else {
                    NetworkResult.Failure(null, t.message)
                }
                return callback.onResponse(
                    this@NetworkResultCall,
                    Response.success(result)
                )
            }
        })

    override fun cloneImpl(): Call<NetworkResult<T>> = NetworkResultCall(proxy.clone())
}

abstract class CallDelegate<TIn, TOut>(
    protected val proxy: Call<TIn>
) : Call<TOut> {
    override fun execute(): Response<TOut> = throw NotImplementedError()
    override fun enqueue(callback: Callback<TOut>) = enqueueImpl(callback)
    override fun clone(): Call<TOut> = cloneImpl()

    override fun cancel() = proxy.cancel()
    override fun request(): Request = proxy.request()
    override fun isExecuted() = proxy.isExecuted
    override fun isCanceled() = proxy.isCanceled

    abstract fun enqueueImpl(callback: Callback<TOut>)
    abstract fun cloneImpl(): Call<TOut>
    override fun timeout(): Timeout = proxy.timeout()
}