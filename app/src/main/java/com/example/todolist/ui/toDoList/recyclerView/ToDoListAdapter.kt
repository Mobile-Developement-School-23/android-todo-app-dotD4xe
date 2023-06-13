package com.example.todolist.ui.toDoList.recyclerView

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.data.model.TodoItem
import com.example.todolist.databinding.ToDoItemBinding


class ToDoListAdapter(
    private val onItemClicked: (Int, TodoItem) -> Unit
) : ListAdapter<TodoItem, ToDoListAdapter.ToDoListViewHolder>(DiffCallback), TouchHelperCallback.TouchHelperAdapter {

    inner class ToDoListViewHolder(private val binding: ToDoItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(toDo: TodoItem) {
            if (toDo.deadline != null) { binding.deadline.text = toDo.deadline }
            else binding.deadline.visibility = View.GONE
            binding.content.text = toDo.content
            binding.checkBox.isChecked = toDo.isDone
            binding.itemLayout.setOnClickListener { onItemClicked.invoke(adapterPosition,toDo) }

            val crossedOut = binding.content.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            val notCrossedOut = binding.content.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

            if (toDo.isDone) {
                binding.content.paintFlags = crossedOut
                binding.content.alpha = 0.3F
            } else {
                binding.content.paintFlags = notCrossedOut
                binding.content.alpha = 1F
            }

            binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
                toDo.isDone = isChecked
                if (isChecked) {
                    binding.content.paintFlags = crossedOut
                    binding.content.alpha = 0.3F
                } else {
                    binding.content.paintFlags = notCrossedOut
                    binding.content.alpha = 1F
                }
            }
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

    private fun deleteItem(position: Int) {
        val newList = currentList.toMutableList()
        newList.removeAt(position)
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