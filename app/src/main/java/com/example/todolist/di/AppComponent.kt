package com.example.todolist.di

import android.app.Application
import android.content.Context
import com.example.todolist.ToDoAppApplication
import com.example.todolist.data.worker.WorkerFactory
import com.example.todolist.presentation.MainActivity
import com.example.todolist.presentation.todo.di.ToDoComponent
import com.example.todolist.presentation.todoList.di.ToDoListComponent
import dagger.BindsInstance
import dagger.Component

@Component(
    modules = [
        DatabaseModule::class,
        NetworkModule::class,
        RepositoryModule::class,
        AppModule::class,
        NetworkModule::class
    ]
)
@ApplicationScope
interface AppComponent {
    @Component.Factory
    interface Factory {
        fun create(
            @BindsInstance app: Application,
            @BindsInstance context: Context,
        ): AppComponent
    }

    fun inject(app: ToDoAppApplication)
    fun inject(activity: MainActivity)

    fun workerFactory(): WorkerFactory
    fun toDoListComponent(): ToDoListComponent.Factory
    fun toDoComponent(): ToDoComponent.Factory
}
