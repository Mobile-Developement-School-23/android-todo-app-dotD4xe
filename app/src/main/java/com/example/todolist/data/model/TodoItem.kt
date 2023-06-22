package com.example.todolist.data.model

import android.os.Parcelable
import com.example.todolist.util.Importance
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class TodoItem(
    val id: String,
    val content: String,
    val importance: Importance,
    val deadline: Date?,
    var isDone: Boolean,
    val dateOfCreation: Date,
    val dateOfChange: Date?
): Parcelable
