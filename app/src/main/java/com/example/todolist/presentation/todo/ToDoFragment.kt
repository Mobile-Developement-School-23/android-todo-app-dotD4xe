package com.example.todolist.presentation.todo

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.ToDoAppApplication
import com.example.todolist.domain.entity.TodoItem
import com.example.todolist.databinding.FragmentToDoBinding
import com.example.todolist.domain.entity.Importance
import com.example.todolist.presentation.util.ALPHA_BUTTON
import com.example.todolist.presentation.util.ALPHA_IMPORTANCE
import com.example.todolist.presentation.util.buildCalendarConstraints
import com.example.todolist.presentation.util.getParcelableCompat
import com.example.todolist.presentation.util.showSnackbar
import com.example.todolist.presentation.util.toDate
import com.example.todolist.presentation.util.toText
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.UUID
import javax.inject.Inject

class ToDoFragment : Fragment() {

    private var _binding: FragmentToDoBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: ToDoViewModel by viewModels { viewModelFactory }

    private var objTodo: TodoItem? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as ToDoAppApplication).appComponent
            .toDoComponent()
            .create()
            .inject(this)
    }

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
        if (objTodo == null) addItemBind()

        /* если редактируем дело */
        else redactorItemBind()

        binding.deleteButton.setOnClickListener { clickDeleteButton() }
        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
        binding.datePicker.setOnCheckedChangeListener { _, isChecked -> showCalendar(isChecked) }
        binding.importance.setOnClickListener { showPopup(binding.importance) }
    }

    private fun clickDeleteButton() {
        if (objTodo != null){
            viewModel.deleteItem(objTodo!!)
            findNavController().navigateUp()
        }
    }

    private fun addItemBind() {
        binding.delete.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorText))
        binding.delete.alpha = ALPHA_BUTTON
        binding.labelDelete.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorText))
        binding.labelDelete.alpha = ALPHA_BUTTON
        binding.save.setOnClickListener {
            if (binding.content.text.isBlank()) showSnackbar("Введите задачу")
            else {
                viewModel.addItem(saveItem(null))
                findNavController().popBackStack()
            }
        }
    }

    private fun redactorItemBind() {
        objTodo?.let { item ->
            binding.content.setText(item.content)
            if (item.deadline != null) {
                binding.deadline.text = item.deadline.toText()
                binding.datePicker.isChecked = true
            }
            binding.textImportance.text = when (item.importance) {
                Importance.LOW -> "Низкий"
                Importance.BASIC -> "Нет"
                else -> "!! Высокий"
            }
            if (binding.textImportance.text == "!! Высокий") binding.textImportance.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.red)
            )
            binding.save.setOnClickListener {
                if (binding.content.text.isBlank()) showSnackbar("Введите задачу")
                else {
                    viewModel.saveItem(saveItem(item))
                    findNavController().popBackStack()
                }
            }
        }
    }
    private fun saveItem(item: TodoItem?): TodoItem {
        val calendar = Calendar.getInstance()

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

        return TodoItem(
            id, content, importance, deadline?.toDate(), null, isDone, dateOfCreation, dateOfChange, lastUpdateBy
        )
    }

    private fun createDatePicker() =
        MaterialDatePicker.Builder.datePicker()
            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            .setNegativeButtonText("Отмена")
            .setPositiveButtonText("Готово")
            .setCalendarConstraints(buildCalendarConstraints())
            .build()

    private fun showCalendar(isChecked: Boolean) {
        if (isChecked) {
            val datePicker = createDatePicker()
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
        spannableString.setSpan(
            ForegroundColorSpan(Color.RED),
            0, spannableString.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        item3.title = spannableString

        popup.setOnMenuItemClickListener { item: MenuItem? -> bindPopup(item) }
        popup.show()
    }

    private fun bindPopup(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.header3 -> {
                binding.textImportance.text = item.title
                binding.textImportance.alpha = 1F
            }
            else -> {
                binding.textImportance.text = item.title
                binding.textImportance.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorText))
                binding.textImportance.alpha = ALPHA_IMPORTANCE
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
