package com.example.todolist.presentation.todoList.recyclerView

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Paint
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.CompoundButtonCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.domain.entity.TodoItem
import com.example.todolist.databinding.ToDoItemBinding
import com.example.todolist.domain.entity.Importance
import com.example.todolist.presentation.util.hide
import com.example.todolist.presentation.util.show
import com.example.todolist.presentation.util.toText

private const val CONTENT_ALPHA = 0.3F
/**
 * ViewHolder class for displaying a single Todo item in the TodoListAdapter.
 * @param binding The data binding instance for the Todo item layout.
 * @param onItemClicked Callback function invoked when the Todo item is clicked.
 * @param onItemChangeListener Optional callback function invoked when the Todo item is changed.
 */
class ToDoListViewHolder(
    private val binding: ToDoItemBinding,
    private val onItemClicked: (Int, TodoItem) -> Unit,
    private val onItemChangeListener: ((TodoItem) -> Unit)?,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(toDo: TodoItem) {

        bindImportance(toDo)
        bindContent(toDo)
        bindDeadline(toDo)

        binding.itemLayout.setOnClickListener { onItemClicked.invoke(adapterPosition, toDo) }

        binding.checkBox.setOnClickListener { onItemChangeListener?.invoke(toDo) }
    }

    private fun bindDeadline(toDo: TodoItem) {
        if (toDo.deadline != null) {
            binding.deadline.show()
            binding.deadline.text = toDo.deadline.toText()
        } else binding.deadline.hide()
    }


    private fun bindContent(toDo: TodoItem) {
        val crossedOut = binding.content.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        val notCrossedOut = binding.content.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

        binding.content.text = toDo.content.replace("\n"," ")
        binding.checkBox.isChecked = toDo.isDone

        if (toDo.isDone) {
            binding.content.paintFlags = crossedOut
            binding.content.alpha = CONTENT_ALPHA
        } else {
            binding.content.paintFlags = notCrossedOut
            binding.content.alpha = 1F
        }
    }

    @SuppressLint("ResourceType")
    private fun bindImportance(toDo: TodoItem) {
        when(toDo.importance) {
            Importance.IMPORTANT -> {
                val colorStateList: ColorStateList? =
                    AppCompatResources
                        .getColorStateList(binding.checkBox.context, R.drawable.check_box_filter_tint_high)
                CompoundButtonCompat.setButtonTintList(binding.checkBox, colorStateList)
                binding.importance.setImageResource(R.drawable.hight_importance)
                binding.importance.show()
            }
            Importance.LOW -> {
                val colorStateList: ColorStateList? =
                    AppCompatResources.getColorStateList(binding.checkBox.context, R.drawable.check_box_filter_tint)
                CompoundButtonCompat.setButtonTintList(binding.checkBox, colorStateList)
                binding.importance.setImageResource(R.drawable.low_importance)
                binding.importance.show()
            }
            else -> {
                val colorStateList: ColorStateList? =
                    AppCompatResources.getColorStateList(binding.checkBox.context, R.drawable.check_box_filter_tint)
                CompoundButtonCompat.setButtonTintList(binding.checkBox, colorStateList)
                binding.importance.hide()
            }
        }
    }
}
