package com.example.todolist.ui.ToDo

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.data.model.TodoItem
import com.example.todolist.databinding.FragmentToDoBinding
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

        objTodo = arguments?.getParcelable("Todo")

        if (objTodo == null) {
            binding.delete.setTextColor(Color.BLACK)
            binding.delete.alpha = 0.15F
            binding.labelDelete.setColorFilter(Color.BLACK)
            binding.labelDelete.alpha = 0.15F
        } else {
            objTodo?.let{
                binding.content.setText(it.content)
                if (it.deadline != "") {
                    binding.deadline.text = it.deadline
                    binding.datePicker.isChecked = true
                }
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            findNavController().navigateUp()
        }

        binding.datePicker.setOnCheckedChangeListener { _, isChecked ->
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
                datePicker.show(parentFragmentManager, "DatePickerDialog")

            } else binding.deadline.text = ""
        }



        val spinner = binding.importance
        val importance = resources.getStringArray(R.array.importance_array)
        val adapter = object : ArrayAdapter<String>(requireContext(), android.R.layout.simple_spinner_item, importance) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val currentView = super.getView(position, convertView, parent)

                if (position == 2) (currentView as TextView).setTextColor(Color.RED)
                return currentView
            }

            override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                val currentView = super.getDropDownView(position, convertView, parent)

                if (position == 2) (currentView as TextView).setTextColor(Color.RED)
                return currentView
            }
        }

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}