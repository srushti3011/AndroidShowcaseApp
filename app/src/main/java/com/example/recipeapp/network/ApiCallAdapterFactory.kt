package com.example.recipeapp.network

import android.util.Log
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiCallAdapterFactory: CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        Log.i("TAG", "Inside ApiCallAdapterFactory")
        if (getRawType(returnType) != Call::class.java)
            return null

        val callType = getParameterUpperBound(0, returnType as ParameterizedType)
        if (getRawType(callType) != ApiResult::class.java)
            return null

        val resultType = getParameterUpperBound(0, callType as ParameterizedType)
        return ApiResultCallAdapter(resultType)
    }
}