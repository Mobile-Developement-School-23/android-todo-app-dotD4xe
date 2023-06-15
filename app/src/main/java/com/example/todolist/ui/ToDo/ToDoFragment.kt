package com.example.todolist.ui.ToDo

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.data.model.TodoItem
import com.example.todolist.data.repository.ToDoRepository
import com.example.todolist.data.repository.ToDoRepository.Companion.itemId
import com.example.todolist.databinding.FragmentToDoBinding
import com.example.todolist.util.Importance
import com.example.todolist.util.toDate
import com.example.todolist.util.toText
import com.example.todolist.util.toast
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class ToDoFragment : Fragment() {

    private var _binding: FragmentToDoBinding? = null
    private val binding get() = _binding!!

    private var objTodo: TodoItem? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToDoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //deprecated но работает прекрасно
        objTodo = arguments?.getParcelable("Todo")

        /* если добавляем дело */
        if (objTodo == null) {
            binding.delete.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorText))
            binding.delete.alpha = 0.20F
            binding.labelDelete.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorText))
            binding.labelDelete.alpha = 0.20F
            binding.save.setOnClickListener {
                if (binding.content.text.isBlank()) toast("введите задачу")
                else {
                    ToDoRepository().addItem(addNewItem())
                    findNavController().navigateUp()
                }
            }
        }
        /* если редактируем дело */
        else {
            objTodo?.let { item ->
                binding.content.setText(item.content)
                if (item.deadline != null) {
                    binding.deadline.text = item.deadline.toText()
                    binding.datePicker.isChecked = true
                }
                binding.textImportance.text = when (item.importance) {
                    Importance.LOW -> "Низкий"
                    Importance.NORMAL -> "Нет"
                    else -> {
                        "!! Высокий"
                    }
                }
                if (binding.textImportance.text == "!! Высокий") binding.textImportance.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.red)
                )
                binding.save.setOnClickListener {
                    if (binding.content.text.isBlank()) toast("введите задачу")
                    else {
                        ToDoRepository().saveItem(saveItem(item))
                        findNavController().navigateUp()
                    }
                }
            }
        }

        binding.deleteButton.setOnClickListener {
            if (objTodo != null){
                ToDoRepository().deleteItem(objTodo!!)
                findNavController().navigateUp()
            }
        }

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        binding.datePicker.setOnCheckedChangeListener { _, isChecked -> showCalendar(isChecked) }

        binding.importance.setOnClickListener { showPopup(binding.importance) }
    }

    private fun addNewItem(): TodoItem {
        val calendar: Calendar = Calendar.getInstance()

        val id = itemId.toString()
        val content = binding.content.text.toString().trim()
        val importance = when(binding.textImportance.text) {
            "Низкий" -> Importance.LOW
            "Нет" -> Importance.NORMAL
            else -> Importance.HIGH
        }
        val deadline = binding.deadline.text.toString().ifBlank { null }
        val isDone = false
        val dateOfCreation = calendar.time
        val dateOfChange = null
        itemId += 1
        return TodoItem(id,content, importance, deadline.toDate(), isDone, dateOfCreation, dateOfChange)
    }

    private fun saveItem(item: TodoItem): TodoItem {
        val calendar: Calendar = Calendar.getInstance()

        val id = item.id
        val content = binding.content.text.toString()
        val importance = when(binding.textImportance.text) {
            "Низкий" -> Importance.LOW
            "Нет" -> Importance.NORMAL
            else -> Importance.HIGH
        }
        val deadline = binding.deadline.text.toString().ifBlank { null }
        val isDone = item.isDone
        val dateOfChange = calendar.time

        return TodoItem(id,content, importance, deadline.toDate(), isDone, item.dateOfCreation, dateOfChange)
    }

    private fun showCalendar(isChecked: Boolean) {
        if (isChecked) {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setNegativeButtonText("Отмена")
                    .setPositiveButtonText("Готово")
                    .build()

            datePicker.addOnNegativeButtonClickListener {
                binding.datePicker.isChecked = false
                binding.deadline.text = ""
            }

            datePicker.addOnPositiveButtonClickListener { selectedDate ->
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = selectedDate

                val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
                val formattedDate = dateFormat.format(calendar.time)

                binding.deadline.text = formattedDate.toString()
            }

            datePicker.addOnCancelListener {
                binding.datePicker.isChecked = false
                binding.deadline.text = ""
            }
            datePicker.show(parentFragmentManager, "DatePickerDialog")
        } else binding.deadline.text = ""
    }

    private fun showPopup(view: View) {
        val wrapper: Context =  ContextThemeWrapper(context, R.style.CustomPopUpStyle)
        val popup = PopupMenu(wrapper, view)
        popup.inflate(R.menu.importance_menu)

        val menu = popup.menu
        val item3 = menu.findItem(R.id.header3)
        val spannableString = SpannableString(item3.title)
        spannableString.setSpan(ForegroundColorSpan(Color.RED), 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        item3.title = spannableString

        popup.setOnMenuItemClickListener { item: MenuItem? ->
            when (item!!.itemId) {
                R.id.header1 -> {
                    binding.textImportance.text = item.title
                    binding.textImportance.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorText))
                    binding.textImportance.alpha = 0.4F
                }
                R.id.header2 -> {
                    binding.textImportance.text = item.title
                    binding.textImportance.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorText))
                    binding.textImportance.alpha = 0.4F
                }
                R.id.header3 -> {
                    binding.textImportance.text = item.title
                    binding.textImportance.alpha = 1F
                }
            }
            true
        }
        popup.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}