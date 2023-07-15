package com.example.todolist.data.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.example.todolist.R
import com.example.todolist.ToDoAppApplication
import com.example.todolist.data.database.dao.TodoItemDao
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

            val todos = todoDao.getTodoItems()

            val currentItems = todos.filter {
                val date = it.deadline?.let { deadline ->
                    Instant.ofEpochSecond(deadline)
                        .atZone(ZoneId.systemDefault()).toLocalDate()
                }
                date == LocalDate.now() && !it.done
            }

            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            currentItems.map{ it.toTodoItem() }.forEach { todo ->
                val importance = when (todo.importance) {
                    Importance.LOW -> context.getString(R.string.low)
                    Importance.BASIC -> context.getString(R.string.no)
                    Importance.IMPORTANT -> context.getString(R.string.hight)
                }
                val notification = NotificationCompat.Builder(context, "channel_id")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle(context.getString(R.string.notification_title))
                    .setContentText(context.getString(R.string.notification_content, todo.content, importance))
                    .build()
                notificationManager.notify(todo.id.hashCode(), notification)
            }
        }
    }
}