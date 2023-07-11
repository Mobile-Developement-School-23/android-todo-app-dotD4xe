package com.example.todolist.presentation.todo.di

import androidx.lifecycle.ViewModel
import com.example.todolist.di.ViewModelKey
import com.example.todolist.presentation.todo.ToDoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ToDoModule {
    @Binds
    @IntoMap
    @ViewModelKey(ToDoViewModel::class)
    fun bindViewModel(viewModel: ToDoViewModel): ViewModel
}
