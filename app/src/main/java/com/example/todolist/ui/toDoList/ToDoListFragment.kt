package com.example.todolist.ui.toDoList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.repository.ToDoRepository.Companion.todoItems
import com.example.todolist.databinding.FragmentToDoListBinding
import com.example.todolist.ui.toDoList.recyclerView.ToDoListAdapter
import com.example.todolist.ui.toDoList.recyclerView.TouchHelperCallback
import com.example.todolist.util.EyeVisibility


class ToDoListFragment : Fragment() {

    private var _binding: FragmentToDoListBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        eyeVisibility()

        binding.done.text = getString(R.string.done, todoItems.filter { it.isDone }.size)

        adapter.setOnChangeItemListener {
            binding.done.text = getString(R.string.done, todoItems.filter { it.isDone }.size)
            /* вызываем для скрывания только что выполненных айтемов если смотрим только не сделанные задачи */
            eyeVisibility()
        }

        binding.visibleTodo.setOnClickListener { clickToEyeVisibility() }

        binding.addItem.setOnClickListener {
            val action = ToDoListFragmentDirections.actionToDoListFragmentToToDoFragment()
            this.findNavController().navigate(action)
        }
    }

    private fun clickToEyeVisibility() {
        if (EyeVisibility.visible) {
            binding.visibleTodo.setImageResource(R.drawable.visibility_off)
            EyeVisibility.visible = false
            val todoItemsCopy = todoItems.toList()
            Log.d("TAG", todoItemsCopy.toString())
            adapter.submitList(todoItemsCopy)
        } else {
            EyeVisibility.visible = true
            binding.visibleTodo.setImageResource(R.drawable.visibility)
            val todoItemsCopy = todoItems.toList()
            adapter.submitList(todoItemsCopy.filter { !it.isDone})
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

    private fun eyeVisibility() {
        if (!EyeVisibility.visible) {
            binding.visibleTodo.setImageResource(R.drawable.visibility_off)
            val todoItemsCopy = todoItems.toList()
            adapter.submitList(todoItemsCopy)
        } else {
            val todoItemsCopy = todoItems.filter { !it.isDone}
            adapter.submitList(todoItemsCopy)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}