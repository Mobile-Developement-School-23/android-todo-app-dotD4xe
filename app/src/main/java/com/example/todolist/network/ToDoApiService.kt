package com.example.todolist.network

import com.example.todolist.network.model.AddTodoRequest
import com.example.todolist.network.model.AddTodoRequest1
import com.example.todolist.network.model.ToDoItem
import com.example.todolist.network.model.ToDoItemResponse
import com.example.todolist.network.model.ToDoListResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface TodoApiService {
    @GET("list")
    suspend fun getTodoList(): ToDoListResponse

    @PATCH("list")
    suspend fun updateTodoList(@Header("X-Last-Known-Revision") revision: Int, @Body todoList: AddTodoRequest1): ToDoListResponse

    @GET("list/{id}")
    suspend fun getTodoItem(@Path("id") id: String): ToDoItemResponse

    @POST("list")
    suspend fun addTodoItem(@Header("X-Last-Known-Revision") revision: Int, @Body element: AddTodoRequest): ToDoItemResponse

    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Int,
        @Body todoItem: AddTodoRequest
    ): ToDoItemResponse

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(@Path("id") id: String, @Header("X-Last-Known-Revision") revision: Int): ToDoItemResponse
}