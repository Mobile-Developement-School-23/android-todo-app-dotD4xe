package com.example.todolist.data.network.retrofit

import okhttp3.Interceptor
import okhttp3.Request

/**
 * Interceptor class for adding an Authorization header with a bearer token to outgoing HTTP requests.
 * @property token The bearer token to be added to the Authorization header.
 */
class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest: Request = chain.request()

        val modifiedRequest: Request = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(modifiedRequest)
    }
}

