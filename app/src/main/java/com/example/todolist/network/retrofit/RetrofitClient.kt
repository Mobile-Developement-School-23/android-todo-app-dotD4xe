package com.example.todolist.network.retrofit

import com.example.todolist.network.api.TodoApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthInterceptor(private val token: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalRequest: Request = chain.request()

        val modifiedRequest: Request = originalRequest.newBuilder()
            .header("Authorization", "Bearer $token")
            .build()

        return chain.proceed(modifiedRequest)
    }
}

object TodoApiClient {
    private const val BASE_URL = "https://beta.mrdekk.ru/todobackend/"
    private const val TOKEN = "overdelicious"

    private val authInterceptor = AuthInterceptor(TOKEN)

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val todoApiService: TodoApiService = retrofit.create(TodoApiService::class.java)
}