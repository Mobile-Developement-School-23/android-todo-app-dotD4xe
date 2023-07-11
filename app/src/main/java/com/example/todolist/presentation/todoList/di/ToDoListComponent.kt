package com.example.todolist.presentation.todoList.di

import com.example.todolist.di.FragmentScope
import com.example.todolist.di.ViewModelBuilderModule
import com.example.todolist.presentation.todoList.ToDoListFragment
import dagger.Subcomponent

@Subcomponent(modules = [TodoListModule::class, ViewModelBuilderModule::class])
@FragmentScope
interface ToDoListComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): ToDoListComponent
    }

    fun inject(fragment: ToDoListFragment)
}
