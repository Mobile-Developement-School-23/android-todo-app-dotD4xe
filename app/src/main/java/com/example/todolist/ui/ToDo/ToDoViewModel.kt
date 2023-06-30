package com.example.todolist.ui.ToDo

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import com.example.todolist.data.model.TodoItem
import com.example.todolist.data.repository.ToDoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToDoViewModel @Inject constructor(
    private val repository: ToDoRepository
) : ViewModel() {

    fun addItem(item: TodoItem) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.addItem(item)
        }
    }

    fun saveItem(item: TodoItem) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.saveItem(item)
        }
    }

    fun deleteItem(item: TodoItem) {
        CoroutineScope(Dispatchers.IO).launch {
            repository.deleteItem(item)
        }
    }
}