package com.example.todolist.presentation.todo.di

import com.example.todolist.di.FragmentScope
import com.example.todolist.di.ViewModelBuilderModule
import com.example.todolist.presentation.todo.ToDoFragment
import dagger.Subcomponent

@Subcomponent(modules = [ToDoModule::class, ViewModelBuilderModule::class])
@FragmentScope
interface ToDoComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): ToDoComponent
    }

    fun inject(fragment: ToDoFragment)
}
