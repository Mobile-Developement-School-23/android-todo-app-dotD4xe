package com.example.todolist.ui.toDoList.recyclerView

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Paint
import android.util.Log
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.widget.CompoundButtonCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.model.TodoItem
import com.example.todolist.databinding.ToDoItemBinding
import com.example.todolist.util.Importance
import com.example.todolist.util.hide
import com.example.todolist.util.show
import com.example.todolist.util.toText

class ToDoListViewHolder(
    private val binding: ToDoItemBinding,
    private val onItemClicked: (Int, TodoItem) -> Unit,
    private val onItemChangeListener: ((TodoItem) -> Unit)?,
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ResourceType")
    fun bind(toDo: TodoItem) {
        Log.d("ayash", toDo.toString())
        val crossedOut = binding.content.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        val notCrossedOut = binding.content.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

        binding.itemLayout.setOnClickListener { onItemClicked.invoke(adapterPosition, toDo) }

        if (toDo.deadline != null) {
            binding.deadline.show()
            binding.deadline.text = toDo.deadline.toText()
        } else {
            binding.deadline.hide()
        }
        binding.content.text = toDo.content.replace("\n"," ")
        binding.checkBox.isChecked = toDo.isDone

        /* если дело выполнено зачеркиваем текст и наоборот если он был зачеркнут */
        if (toDo.isDone) {
            binding.content.paintFlags = crossedOut
            binding.content.alpha = 0.3F
        } else {
            binding.content.paintFlags = notCrossedOut
            binding.content.alpha = 1F
        }

        /* выставляем стиль CheckBox и картинки в зависимости от importance */
        when(toDo.importance) {
            Importance.IMPORTANT -> {
                val colorStateList: ColorStateList? =
                    AppCompatResources.getColorStateList(binding.checkBox.context, R.drawable.check_box_filter_tint_high)
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

        binding.checkBox.setOnCheckedChangeListener { _, _ ->
            onItemChangeListener?.invoke(toDo)
            Log.d("ayash", "from holder " + toDo.toString())
        }
    }


}
