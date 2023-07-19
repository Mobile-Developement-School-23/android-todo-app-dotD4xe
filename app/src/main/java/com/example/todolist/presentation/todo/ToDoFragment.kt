package com.example.todolist.presentation.todo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Divider
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.todolist.ToDoAppApplication
import com.example.todolist.domain.entity.TodoItem
import com.example.todolist.presentation.todo.components.AppBar
import com.example.todolist.presentation.todo.components.AppBarPreview
import com.example.todolist.presentation.todo.components.ButtonDelete
import com.example.todolist.presentation.todo.components.ButtonDeletePreview
import com.example.todolist.presentation.todo.components.InputField
import com.example.todolist.presentation.todo.components.InputFieldPreview
import com.example.todolist.presentation.todo.components.TaskEditDateField
import com.example.todolist.presentation.todo.components.TaskEditDateFieldPreview
import com.example.todolist.presentation.todo.components.TodoImportance
import com.example.todolist.presentation.todo.components.TodoImportancePreview
import com.example.todolist.presentation.todo.theme.ExtendedTheme
import com.example.todolist.presentation.todo.theme.TodoAppTheme
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
                TodoAppTheme() {
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
                onAction = viewModel::onAction,
                content = state.content
            ) },
            containerColor = ExtendedTheme.colors.backPrimary
        ) { paddingValues ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
            ) {

                InputField(
                    state.content,
                    viewModel::onAction
                )

                TodoImportance(
                    state.importance,
                    viewModel::onAction
                )

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    color = ExtendedTheme.colors.supportOverlay
                )

                TaskEditDateField(state, viewModel::onAction)

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp),
                    color = ExtendedTheme.colors.supportOverlay
                )

                ButtonDelete(
                    !state.isAddItem,
                    viewModel::onAction,
                    navController = findNavController()
                )

            }
        }
    }

    @Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_NO)
    @Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
    @Composable
    fun ToDoScreenPreview() {
        TodoAppTheme() {
            Scaffold(
                topBar = { AppBarPreview() },
                containerColor = ExtendedTheme.colors.backPrimary
            ) { paddingValues ->

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {

                    InputFieldPreview()

                    TodoImportancePreview()

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        color = ExtendedTheme.colors.supportOverlay
                    )

                    TaskEditDateFieldPreview()

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 8.dp),
                        color = ExtendedTheme.colors.supportOverlay
                    )

                    ButtonDeletePreview()
                }
            }
        }
    }
}



