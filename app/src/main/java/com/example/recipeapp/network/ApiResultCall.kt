package com.example.recipeapp.network

import android.util.Log
import okhttp3.Request
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ApiResultCall<T: Any>(
    private val call: Call<T>
): Call<ApiResult<T>> {
    override fun clone(): Call<ApiResult<T>> {
        return ApiResultCall(call.clone())
    }

    override fun execute(): Response<ApiResult<T>> {
        throw NotImplementedError()
    }

    override fun isExecuted(): Boolean {
        return call.isExecuted
    }

    override fun cancel() {
        call.cancel()
    }

    override fun isCanceled(): Boolean {
        return call.isCanceled
    }

    override fun request(): Request {
        return call.request()
    }

    override fun timeout(): Timeout {
        return call.timeout()
    }

    override fun enqueue(callback: Callback<ApiResult<T>>) {
        Log.i("TAG", "Inside ApiResultCall's enqueue")
        call.enqueue(object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                Log.i("TAG", "Inside ApiResultCall's enqueue's onResponse")
                val result = getApiResult(response)
                callback.onResponse(this@ApiResultCall, Response.success(result))
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                Log.i("TAG", "Inside ApiResultCall's enqueue's onFailure")
                val result = ApiException<T>(t)
                Log.i("TAG", result.toString())
                callback.onResponse(this@ApiResultCall, Response.success(result))
            }
        })
    }

    private fun <T: Any> getApiResult(response: Response<T>): ApiResult<T> {
        if (response.isSuccessful) {
            response.body()?.let {
                return ApiSuccess(it)
            }
        } else {
            if (response.code() in 400..499) {
//                val errorResponse = response.errorBody()?.string()?.let {
//                    Json.decodeFromString<ApiErrorModel>(it)
//                }
                return ApiError(response.code(), "Some error occurred")
            } else if (response.code() in 500..599) {
                return ApiError(response.code(), "Server problem")
            }
            response.errorBody()?.string()?.let { Log.i("TAG", it) }
        }
        return ApiException(Exception("Some exception occurred"))
    }
}