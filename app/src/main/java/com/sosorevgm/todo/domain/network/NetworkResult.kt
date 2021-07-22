package com.sosorevgm.todo.domain.network

sealed class NetworkResult<out T> {
    data class Success<T>(val body: T) : NetworkResult<T>()
    data class Failure(val statusCode: Int?, val error: String?) : NetworkResult<Nothing>()
}

inline fun <T> NetworkResult<T>.fold(
    onSuccess: (body: T) -> Unit = {},
    onFailure: (statusCode: Int?, error: String?) -> Unit = { _, _ -> }
) {
    when (this) {
        is NetworkResult.Success -> onSuccess(body)
        is NetworkResult.Failure -> onFailure(statusCode, error)
    }
}