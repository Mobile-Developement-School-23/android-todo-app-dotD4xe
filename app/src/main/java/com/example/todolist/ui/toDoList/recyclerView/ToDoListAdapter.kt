package com.example.todolist.ui.toDoList.recyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.todolist.data.model.TodoItem
import com.example.todolist.databinding.ToDoItemBinding


class ToDoListAdapter(
    private val onItemClicked: (Int, TodoItem) -> Unit
) : ListAdapter<TodoItem, ToDoListViewHolder>(DiffCallback), TouchHelperCallback.TouchHelperAdapter {

    private var onItemChangeListener: ((TodoItem) -> Unit)? = null
    private var onItemDeleteListener: ((TodoItem) -> Unit)? = null

    /* Callback функция для обновления выполненных дел в ToDoListFragment */
    fun setOnChangeItemListener(listener: (TodoItem) -> Unit) { onItemChangeListener = listener }

    fun setOnDeleteItemListener(listener: (TodoItem) -> Unit) {onItemDeleteListener = listener}

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

    override fun onItemDismiss(position: Int) { onItemDeleteListener?.invoke(getItem(position)) }

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