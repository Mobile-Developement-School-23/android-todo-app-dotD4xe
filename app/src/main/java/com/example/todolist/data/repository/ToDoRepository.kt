package com.example.todolist.data.repository

import com.example.todolist.data.model.TodoItem
import com.example.todolist.util.Importance
import com.example.todolist.util.toDate
import java.util.Calendar

class ToDoRepository {

    fun addItem(item: TodoItem) {
        todoItems.add(item)
    }

    fun saveItem(item: TodoItem) {
        var index = 0
        for (i in 0 until todoItems.size) {
            if (item.id == todoItems[i].id) {
                index = i
                break
            }
        }
        todoItems[index] = item
    }

    fun deleteItem(item: TodoItem) {
        todoItems.removeIf { item.id == it.id }
    }

    companion object {
        val calendar: Calendar = Calendar.getInstance()

        var itemId = 12
        private val todoItem = TodoItem("1", "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обр…", Importance.NORMAL, null, false, calendar.time, null)
        private val todoItem2 = todoItem.copy(id = "2", content = "Привет, что да как", importance = Importance.LOW)
        private val todoItem3 = todoItem.copy(id = "3", content = "Доделать лабу по ОБД потому что препод очень душный и сильно валит, если не сделаю мне конец",  importance = Importance.NORMAL, deadline = "24 августа 2022".toDate())
        private val todoItem4 = todoItem.copy(id = "4", content = "Украсть машину", importance = Importance.HIGH, deadline = "16 августа 2022".toDate(), isDone = false)
        private val todoItem5 = todoItem.copy(id = "5", content = "что-то", importance = Importance.LOW, deadline = "21 августа 2022".toDate(), isDone = false)
        private val todoItem6 = todoItem.copy(id = "6", content = "Я устал", importance = Importance.HIGH, isDone = false)
        private val todoItem7 = todoItem.copy(id = "7", content = "Уехать из России", importance = Importance.NORMAL, deadline = "21 июля 2025".toDate(), isDone = false)
        private val todoItem8 = todoItem.copy(id = "8", content = "Что-то надо срочно делать", importance = Importance.NORMAL, isDone = false)
        private val todoItem9 = todoItem.copy(id = "9", content = "Ку ку", importance = Importance.HIGH, isDone = false)
        private val todoItem10 = todoItem.copy(id = "10", content = "что-то", importance = Importance.LOW, deadline = "21 августа 2022".toDate(), isDone = false)
        private val todoItem11 = todoItem.copy(id = "11", content = "что-то", importance = Importance.LOW, deadline = "21 августа 2022".toDate(), isDone = false)

        val todoItems = mutableListOf(todoItem, todoItem2, todoItem3, todoItem4, todoItem5, todoItem6, todoItem7, todoItem8, todoItem9, todoItem10, todoItem11)
    }
}