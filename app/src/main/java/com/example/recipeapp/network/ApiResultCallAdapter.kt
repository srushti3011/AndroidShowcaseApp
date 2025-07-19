package com.example.recipeapp.network

import android.util.Log
import retrofit2.Call
import retrofit2.CallAdapter
import java.lang.reflect.Type

class ApiResultCallAdapter(
    private val responseType: Type
): CallAdapter<Type, Call<ApiResult<Type>>> {
    override fun responseType(): Type {
        Log.i("TAG", "Inside ApiResultCallAdapter's responseType method ${responseType}")
        return responseType
    }

    override fun adapt(call: Call<Type>): Call<ApiResult<Type>> {
        Log.i("TAG", "Inside ApiResultCallAdapter's adapt method")
        return ApiResultCall(call)
    }
}