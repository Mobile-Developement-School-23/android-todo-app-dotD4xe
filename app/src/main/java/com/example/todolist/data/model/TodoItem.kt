package com.example.todolist.data.model

import android.os.Parcelable
import com.example.todolist.util.Importance
import kotlinx.parcelize.Parcelize


@Parcelize
data class TodoItem(
    val id: String,
    val content: String,
    val importance: Importance,
    val deadline: String?,
    var isDone: Boolean,
    val dateOfCreation: String,
    val dateOfChange: String?
): Parcelable
