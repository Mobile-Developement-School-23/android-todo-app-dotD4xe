package com.example.todolist.presentation.todoList

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.ToDoAppApplication
import com.example.todolist.databinding.FragmentToDoListBinding
import com.example.todolist.presentation.todoList.model.TodoListState
import com.example.todolist.presentation.todoList.recyclerView.ToDoListAdapter
import com.example.todolist.presentation.todoList.recyclerView.TouchHelperCallback
import com.example.todolist.presentation.util.repeatOnCreated
import com.example.todolist.presentation.util.showSnackbar
import javax.inject.Inject

/**
 * Main fragment of the application.
 */
class ToDoListFragment : Fragment() {

    private var _binding: FragmentToDoListBinding? = null
    private val binding get() = _binding!!

    private var isPermissionRequested = true

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel: ToDoListViewModel by viewModels { viewModelFactory }

    private lateinit var recyclerView: RecyclerView

    private val adapter by lazy {
        ToDoListAdapter(
            onItemClicked = { _, item ->
                val action = ToDoListFragmentDirections.actionToDoListFragmentToToDoFragment(item)
                findNavController().navigate(action)
            }
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as ToDoAppApplication).appComponent
            .toDoListComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToDoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRefreshLayoutEnabledState()
        initRecyclerView()
        subscribeOnViewModel()
        requestPermission()

        adapter.setOnChangeItemListener { viewModel.checkTodoItem(it) }

        adapter.setOnDeleteItemListener { viewModel.deleteItem(it) }

        binding.visibleTodo.setOnClickListener { viewModel.changeCompletedTodosVisibility() }

        binding.settings.setOnClickListener {
            val action = ToDoListFragmentDirections.actionToDoListFragmentToSettingsFragment()
            findNavController().navigate(action)
        }

        binding.addItem.setOnClickListener {
            val action = ToDoListFragmentDirections.actionToDoListFragmentToToDoFragment()
            this.findNavController().navigate(action)
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.loadTodoItems()
            if (viewModel.todoItems.value.error.isNotBlank()) showSnackbar(viewModel.todoItems.value.error)
            binding.refreshLayout.isRefreshing = false
        }
    }

    private fun subscribeOnViewModel() {
        viewModel.todoItems.repeatOnCreated(this) {
            showContent(it)
        }
    }

    private fun showContent(items: TodoListState) {
        if (items.error.isNotBlank()) showSnackbar(items.error)
        eyeVisibility(items)
        adapter.submitList(items.listItems)
    }

    private fun eyeVisibility(items: TodoListState) {
        binding.done.text = getString(R.string.done, items.completed)

        if (items.isDone) {
            binding.visibleTodo.setImageResource(R.drawable.visibility_off)
        } else {
            binding.visibleTodo.setImageResource(R.drawable.visibility)
        }
    }

    private fun setupRefreshLayoutEnabledState() {
        binding.appBarLayout.addOnOffsetChangedListener{ _, verticalOffset ->
            binding.refreshLayout.isEnabled = verticalOffset == 0
        }
    }

    private fun initRecyclerView() {
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val itemTouchHelperCallback = TouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(itemTouchHelperCallback)
        touchHelper.attachToRecyclerView(recyclerView)
    }

    private fun requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when { (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED) -> { }
                (ActivityCompat.shouldShowRequestPermissionRationale(
                    requireActivity(),
                    Manifest.permission.POST_NOTIFICATIONS
                )) -> { }
                else -> { requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS) }
            }
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            showSnackbar("Разрешение на отправку уведомлений не было предоставлено")
        }
    }

    override fun onPause() {
        super.onPause()
        binding.refreshLayout.isRefreshing = false
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
