package com.example.recipeapp.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class CustomInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val url = chain.request()
            .url
            .newBuilder()
            .addQueryParameter("apiKey", "ce99995ce7da41ceaa89248f61338df4")
            .build()
        Log.i("TAG", url.toString())
        val request = chain.request()
            .newBuilder()
            .url(url)
            .build()
        return chain.proceed(request)
    }
}