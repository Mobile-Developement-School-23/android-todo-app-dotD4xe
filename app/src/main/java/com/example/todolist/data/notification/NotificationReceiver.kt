package com.example.todolist.data.notification

import android.app.Application
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.todolist.R
import com.example.todolist.ToDoAppApplication
import com.example.todolist.data.database.dao.TodoItemDao
import com.example.todolist.data.database.entity.TodoItemEntity
import com.example.todolist.data.database.entity.toTodoItem
import com.example.todolist.di.DaggerAppComponent
import com.example.todolist.domain.entity.Importance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

class NotificationReceiver : BroadcastReceiver() {
    @Inject
    lateinit var todoDao: TodoItemDao

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context?, intent: Intent?) {
        val appComponent = DaggerAppComponent.factory().create(
            context?.applicationContext as ToDoAppApplication, context
        )
        appComponent.inject(this)

        CoroutineScope(Dispatchers.IO).launch {
            Log.d("ayash", "зешел")

            val todos1 = todoDao.getTodoItems()

                    val todoDate1 = todos1[0].deadline?.let { deadline ->
                        Instant.ofEpochSecond(deadline)
                            .atZone(ZoneId.systemDefault()).toLocalDate()
                    }
            Log.d("ayash", (todoDate1 == LocalDate.now()).toString())
            Log.d("ayash", "${todoDate1.toString()}  ${LocalDate.now().toString()}")


            val todos = todoDao.getTodoItems()
                .filter {
                    val todoDate = it.deadline?.let { deadline ->
                        Instant.ofEpochSecond(deadline)
                            .atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    todoDate == LocalDate.now() && !it.done
                }.map { it.toTodoItem() }


            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            //текст
            todos.forEach { todo ->
                val todoUrgency = when (todo.importance) {
                    Importance.LOW -> "low"
                    Importance.BASIC -> "basic"
                    Importance.IMPORTANT -> "Important"
                }
                val notification = NotificationCompat.Builder(context, "channel_id")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("hello ")
                    .setContentText("${todo.content} $todoUrgency")
                    .build()
                notificationManager.notify(todo.id.hashCode(), notification)
            }
        }
    }
}