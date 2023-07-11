package com.example.todolist.data.network.api

import com.example.todolist.data.network.model.AddTodoRequest
import com.example.todolist.data.network.model.AddTodoRequestList
import com.example.todolist.data.network.model.ToDoItemResponse
import com.example.todolist.data.network.model.ToDoListResponse
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Retrofit service interface for interacting with the Todo API.
 */
interface TodoApiService {
    @GET("list")
    suspend fun getTodoList(): ToDoListResponse

    @PATCH("list")
    suspend fun updateTodoList(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body todoList: AddTodoRequestList
    ): ToDoListResponse

    @POST("list")
    suspend fun addTodoItem(
        @Header("X-Last-Known-Revision") revision: Int,
        @Body element: AddTodoRequest
    ): ToDoItemResponse

    @PUT("list/{id}")
    suspend fun updateTodoItem(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Int,
        @Body todoItem: AddTodoRequest
    ): ToDoItemResponse

    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Int
    ): ToDoItemResponse
}
