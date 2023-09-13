package ru.kovsheful.wallcraft.core

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response
import ru.kovsheful.wallcraft.BuildConfig


class ApiKeyInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        // Add authorization header with updated authorization value to intercepted request
        val requestBuilder = original.newBuilder()
            .header("Authorization", BuildConfig.API_KEY)
            .build()

        //Continue request
        return chain.proceed(requestBuilder)
    }
}
