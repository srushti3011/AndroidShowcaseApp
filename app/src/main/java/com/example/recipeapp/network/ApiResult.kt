package com.example.recipeapp.network

sealed interface ApiResult<T: Any?>
data class ApiError<T: Any?>(val code: Int, val message: String): ApiResult<T>
data class ApiSuccess<T: Any?>(val dataReceived: T): ApiResult<T>
data class ApiException<T: Any?>(val exception: Throwable): ApiResult<T>

suspend fun <T: Any?> ApiResult<T>.onSuccess(
    executable: suspend (T) -> Unit
): ApiResult<T> {
    return apply {
        if (this is ApiSuccess<T>) {
            executable(dataReceived)
        }
    }
}
suspend fun <T: Any?> ApiResult<T>.onException(
    executable: suspend (Throwable) -> Unit
): ApiResult<T> {
    return apply {
        if (this is ApiException<T>) {
            executable(exception)
        }
    }
}
suspend fun <T: Any?> ApiResult<T>.onError(
    executable: suspend (Int, String) -> Unit
): ApiResult<T> {
    return apply {
        if (this is ApiError<T>) {
            executable(code, message)
        }
    }
}