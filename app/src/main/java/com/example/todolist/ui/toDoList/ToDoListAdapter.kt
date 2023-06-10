package com.example.todolist.ui.toDoList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.model.TodoItem
import com.example.todolist.databinding.ToDoItemBinding

class ToDoListAdapter(
    private val onItemClicked: (Int, TodoItem) -> Unit
) : ListAdapter<TodoItem, ToDoListAdapter.ToDoListViewHolder>(DiffCallback) {

    inner class ToDoListViewHolder(private val binding: ToDoItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(toDo: TodoItem) {
            binding.content.text = toDo.content
            binding.itemLayout.setOnClickListener { onItemClicked.invoke(adapterPosition,toDo) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoListViewHolder {
        return ToDoListViewHolder(
            ToDoItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ToDoListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

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