package com.example.todolist.ui.toDoList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.databinding.FragmentToDoListBinding
import com.example.todolist.ui.toDoList.model.TodoListState
import com.example.todolist.ui.toDoList.recyclerView.ToDoListAdapter
import com.example.todolist.ui.toDoList.recyclerView.TouchHelperCallback
import com.example.todolist.util.NetworkStateReceiver
import com.example.todolist.util.repeatOnCreated
import com.example.todolist.util.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ToDoListFragment : Fragment() {

    private var _binding: FragmentToDoListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ToDoListViewModel by viewModels()

    private lateinit var recyclerView: RecyclerView

    private lateinit var networkStateReceiver: NetworkStateReceiver

    private val adapter by lazy {
        ToDoListAdapter(
            onItemClicked = { _, item ->
                findNavController().navigate(R.id.toDoFragment,Bundle().apply {
                    putParcelable("Todo",item)
                })
            }
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentToDoListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        networkStateReceiver.register()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRefreshLayoutEnabledState()
        initRecyclerView()
        subscribeOnViewModel()

        adapter.setOnChangeItemListener { viewModel.checkTodoItem(it) }

        adapter.setOnDeleteItemListener { viewModel.deleteItem(it) }

        binding.visibleTodo.setOnClickListener { viewModel.changeCompletedTodosVisibility() }

        binding.addItem.setOnClickListener {
            val action = ToDoListFragmentDirections.actionToDoListFragmentToToDoFragment()
            this.findNavController().navigate(action)
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.loadTodoItems()
            if (viewModel.todoItems.value.error.isNotBlank()) showSnackbar(viewModel.todoItems.value.error)
            binding.refreshLayout.isRefreshing = false
        }

        networkStateReceiver = NetworkStateReceiver(requireContext()) {
            viewModel.syncData()
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

    override fun onPause() {
        super.onPause()
        binding.refreshLayout.isRefreshing = false
        networkStateReceiver.unregister()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}