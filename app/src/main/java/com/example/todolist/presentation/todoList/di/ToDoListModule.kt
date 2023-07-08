package com.example.todolist.presentation.todoList.di

import androidx.lifecycle.ViewModel
import com.example.todolist.di.ViewModelKey
import com.example.todolist.presentation.todoList.ToDoListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface TodoListModule {
    @Binds
    @IntoMap
    @ViewModelKey(ToDoListViewModel::class)
    fun bindViewModel(viewModel: ToDoListViewModel): ViewModel
}
