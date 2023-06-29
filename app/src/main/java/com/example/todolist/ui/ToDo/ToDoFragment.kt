package com.example.todolist.ui.ToDo

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.navigation.navOptions
import com.example.todolist.R
import com.example.todolist.data.model.TodoItem
import com.example.todolist.databinding.FragmentToDoBinding
import com.example.todolist.util.Importance
import com.example.todolist.util.buildCalendarConstraints
import com.example.todolist.util.getParcelableCompat
import com.example.todolist.util.showSnackbar
import com.example.todolist.util.toDate
import com.example.todolist.util.toText
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.Calendar
import java.util.Locale
import java.util.UUID

@AndroidEntryPoint
class ToDoFragment : Fragment() {

    private var _binding: FragmentToDoBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ToDoViewModel by viewModels()

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

        objTodo = arguments?.getParcelableCompat("Todo")

        /* если добавляем дело */
        if (objTodo == null) {
            binding.delete.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorText))
            binding.delete.alpha = 0.20F
            binding.labelDelete.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorText))
            binding.labelDelete.alpha = 0.20F
            binding.save.setOnClickListener {
                if (binding.content.text.isBlank()) showSnackbar("Введите задачу")
                else {
                    viewModel.addItem(saveItem(null))
                    findNavController().popBackStack()
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
                    Importance.BASIC -> "Нет"
                    else -> {
                        "!! Высокий"
                    }
                }
                if (binding.textImportance.text == "!! Высокий") binding.textImportance.setTextColor(
                    ContextCompat.getColor(requireContext(), R.color.red)
                )
                binding.save.setOnClickListener {
                    if (binding.content.text.isBlank()) showSnackbar("Введите задачу")
                    else {
                        Log.d("ayash", "save item")
                        viewModel.saveItem(saveItem(item))
                        findNavController().popBackStack()
                    }
                }
            }
        }

        binding.deleteButton.setOnClickListener {
            if (objTodo != null){
                viewModel.deleteItem(objTodo!!)
                findNavController().navigateUp()
            }
        }

        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

        binding.datePicker.setOnCheckedChangeListener { _, isChecked -> showCalendar(isChecked) }

        binding.importance.setOnClickListener { showPopup(binding.importance) }
    }
    private fun saveItem(item: TodoItem?): TodoItem {
        val calendar: Calendar = Calendar.getInstance()

        val id = item?.id ?: UUID.randomUUID().toString()
        val content = binding.content.text.toString().trim()
        val importance = when (binding.textImportance.text) {
            "Низкий" -> Importance.LOW
            "Нет" -> Importance.BASIC
            else -> Importance.IMPORTANT
        }
        val deadline = binding.deadline.text.toString().ifBlank { null }
        val isDone = item?.isDone ?: false
        val dateOfCreation = item?.dateOfCreation ?: calendar.time
        val dateOfChange = calendar.time
        val lastUpdateBy = "test"

        return TodoItem(id, content, importance, deadline?.toDate(), null, isDone, dateOfCreation, dateOfChange, lastUpdateBy)
    }
    //совместить 2 метода которые внизу

    private fun showCalendar(isChecked: Boolean) {
        if (isChecked) {
            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .setNegativeButtonText("Отмена")
                    .setPositiveButtonText("Готово")
                    .setCalendarConstraints(buildCalendarConstraints())
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