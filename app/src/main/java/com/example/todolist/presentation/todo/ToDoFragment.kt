package com.example.todolist.presentation.todo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todolist.ToDoAppApplication
import com.example.todolist.domain.entity.TodoItem
import com.example.todolist.databinding.FragmentToDoBinding
import com.example.todolist.presentation.todo.components.AppBar
import com.example.todolist.presentation.todo.components.TaskEditDateField
import com.example.todolist.presentation.todo.components.inputField
import com.example.todolist.presentation.todo.components.todoImportance
import com.example.todolist.presentation.todo.model.TodoAction
import com.example.todolist.presentation.todo.theme.ExtendedTheme
import com.example.todolist.presentation.todo.theme.todoAppTheme
import com.example.todolist.presentation.util.getParcelableCompat
import javax.inject.Inject

class ToDoFragment : Fragment() {

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
    ) = ComposeView(requireContext()).apply {
            setContent {
                todoAppTheme() {
                    ToDoScreen()
                }
            }
        }

    @Composable
    fun ToDoScreen() {
        objTodo = arguments?.getParcelableCompat("Todo")
        viewModel.setupTodo(objTodo)
        val state by viewModel.todoState.collectAsState()


        Scaffold(
            topBar = { AppBar(
                navController = findNavController(),
                viewModel::onAction
            ) },
            containerColor = ExtendedTheme.colors.backPrimary
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {

                inputField(
                    state.content,
                    viewModel::onAction
                )

                todoImportance(
                    state.importance,
                    viewModel::onAction
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = ExtendedTheme.colors.supportOverlay
                )

//                Spacer(modifier = Modifier.height(50.dp))

                TaskEditDateField(state, viewModel::onAction)

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp),
                    color = ExtendedTheme.colors.supportOverlay
                )

                Spacer(modifier = Modifier.height(50.dp))

            }
        }
    }
}


//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        objTodo = arguments?.getParcelableCompat("Todo")
//
//        /* если добавляем дело */
//        if (objTodo == null) addItemBind()
//
//        /* если редактируем дело */
//        else redactorItemBind()
//
//        binding.deleteButton.setOnClickListener { clickDeleteButton() }
//        binding.toolbar.setNavigationOnClickListener { findNavController().navigateUp() }
//        binding.datePicker.setOnCheckedChangeListener { _, isChecked -> showCalendar(isChecked) }
//        binding.importance.setOnClickListener { showPopup(binding.importance) }
//    }
//
//    private fun clickDeleteButton() {
//        if (objTodo != null){
//            viewModel.deleteItem(objTodo!!)
//            findNavController().navigateUp()
//        }
//    }
//
//    private fun addItemBind() {
//        binding.delete.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorText))
//        binding.delete.alpha = ALPHA_BUTTON
//        binding.labelDelete.setColorFilter(ContextCompat.getColor(requireContext(), R.color.colorText))
//        binding.labelDelete.alpha = ALPHA_BUTTON
//        binding.save.setOnClickListener {
//            if (binding.content.text.isBlank()) showSnackbar("Введите задачу")
//            else {
//                viewModel.addItem(saveItem(null))
//                findNavController().popBackStack()
//            }
//        }
//    }
//
//    private fun redactorItemBind() {
//        objTodo?.let { item ->
//            binding.content.setText(item.content)
//            if (item.deadline != null) {
//                binding.deadline.text = item.deadline.toText()
//                binding.datePicker.isChecked = true
//            }
//            binding.textImportance.text = when (item.importance) {
//                Importance.LOW -> "Низкий"
//                Importance.BASIC -> "Нет"
//                else -> "!! Высокий"
//            }
//            if (binding.textImportance.text == "!! Высокий") binding.textImportance.setTextColor(
//                ContextCompat.getColor(requireContext(), R.color.red)
//            )
//            binding.save.setOnClickListener {
//                if (binding.content.text.isBlank()) showSnackbar("Введите задачу")
//                else {
//                    viewModel.saveItem(saveItem(item))
//                    findNavController().popBackStack()
//                }
//            }
//        }
//    }
//    private fun saveItem(item: TodoItem?): TodoItem {
//        val calendar = Calendar.getInstance()
//
//        val id = item?.id ?: UUID.randomUUID().toString()
//        val content = binding.content.text.toString().trim()
//        val importance = when (binding.textImportance.text) {
//            "Низкий" -> Importance.LOW
//            "Нет" -> Importance.BASIC
//            else -> Importance.IMPORTANT
//        }
//        val deadline = binding.deadline.text.toString().ifBlank { null }
//        val isDone = item?.isDone ?: false
//        val dateOfCreation = item?.dateOfCreation ?: calendar.time
//        val dateOfChange = calendar.time
//        val lastUpdateBy = "test"
//
//        return TodoItem(
//            id, content, importance, deadline?.toDate(), null, isDone, dateOfCreation, dateOfChange, lastUpdateBy
//        )
//    }
//
//    private fun createDatePicker() =
//        MaterialDatePicker.Builder.datePicker()
//            .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
//            .setNegativeButtonText("Отмена")
//            .setPositiveButtonText("Готово")
//            .setCalendarConstraints(buildCalendarConstraints())
//            .build()
//
//    private fun showCalendar(isChecked: Boolean) {
//        if (isChecked) {
//            val datePicker = createDatePicker()
//            datePicker.addOnNegativeButtonClickListener {
//                binding.datePicker.isChecked = false
//                binding.deadline.text = ""
//            }
//            datePicker.addOnPositiveButtonClickListener { selectedDate ->
//                val calendar = Calendar.getInstance()
//                calendar.timeInMillis = selectedDate
//                val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale("ru"))
//                val formattedDate = dateFormat.format(calendar.time)
//
//                binding.deadline.text = formattedDate.toString()
//            }
//            datePicker.addOnCancelListener {
//                binding.datePicker.isChecked = false
//                binding.deadline.text = ""
//            }
//            datePicker.show(parentFragmentManager, "DatePickerDialog")
//        } else binding.deadline.text = ""
//    }
//
//    private fun showPopup(view: View) {
//        val wrapper: Context =  ContextThemeWrapper(context, R.style.CustomPopUpStyle)
//        val popup = PopupMenu(wrapper, view)
//        popup.inflate(R.menu.importance_menu)
//
//        val menu = popup.menu
//        val item3 = menu.findItem(R.id.header3)
//        val spannableString = SpannableString(item3.title)
//        spannableString.setSpan(
//            ForegroundColorSpan(Color.RED),
//            0, spannableString.length,
//            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
//        )
//        item3.title = spannableString
//
//        popup.setOnMenuItemClickListener { item: MenuItem? -> bindPopup(item) }
//        popup.show()
//    }
//
//    private fun bindPopup(item: MenuItem?): Boolean {
//        when (item!!.itemId) {
//            R.id.header3 -> {
//                binding.textImportance.text = item.title
//                binding.textImportance.alpha = 1F
//            }
//            else -> {
//                binding.textImportance.text = item.title
//                binding.textImportance.setTextColor(ContextCompat.getColor(requireContext(), R.color.colorText))
//                binding.textImportance.alpha = ALPHA_IMPORTANCE
//            }
//        }
//        return true
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        _binding = null
//    }


