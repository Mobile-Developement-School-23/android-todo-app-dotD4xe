package com.example.todolist.ui.toDoList.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.todolist.data.model.TodoItem
import com.example.todolist.data.repository.ToDoRepository.Companion.todoItems
import com.example.todolist.databinding.ToDoItemBinding


class ToDoListAdapter(
    private val onItemClicked: (Int, TodoItem) -> Unit
) : ListAdapter<TodoItem, ToDoListViewHolder>(DiffCallback), TouchHelperCallback.TouchHelperAdapter {

    private var onItemChangeListener: (() -> Unit)? = null

    /* Callback функция для обновления выполненных дел в ToDoListFragment */
    fun setOnChangeItemListener(listener: () -> Unit) {
        onItemChangeListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoListViewHolder {
        return ToDoListViewHolder(
            ToDoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClicked,
            onItemChangeListener,
        )
    }

    override fun onBindViewHolder(holder: ToDoListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun deleteItem(position: Int) {
        val newList = currentList.toMutableList()
        todoItems.removeIf { newList[position].id == it.id }
        newList.removeAt(position)
        onItemChangeListener?.invoke()
        submitList(newList)
    }

    override fun onItemDismiss(position: Int) = deleteItem(position)

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<TodoItem>() {
            override fun areItemsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TodoItem, newItem: TodoItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}