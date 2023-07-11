package com.example.todolist.di

import com.example.todolist.data.network.retrofit.AuthInterceptor
import com.example.todolist.data.network.api.TodoApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
interface NetworkModule {
    companion object {
        private const val BASE_URL = "https://beta.mrdekk.ru/todobackend/"
        private const val TOKEN = "overdelicious"

        @Provides
        @ApplicationScope
        fun provideOkHttpClient(): OkHttpClient {
            val authInterceptor = AuthInterceptor(TOKEN)
            return OkHttpClient.Builder()
                .addInterceptor(authInterceptor)
                .build()
        }

        @Provides
        @ApplicationScope
        fun provideRetrofit(client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

        @Provides
        @ApplicationScope
        fun provideTodoApiService(retrofit: Retrofit): TodoApiService {
            return retrofit.create(TodoApiService::class.java)
        }
    }

}
