package com.example.todolist.ui.toDoList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.todolist.R
import com.example.todolist.data.model.TodoItem
import com.example.todolist.databinding.FragmentToDoListBinding
import com.example.todolist.util.Importance


class ToDoListFragment : Fragment() {

    private var _binding: FragmentToDoListBinding? = null
    private val binding get() = _binding!!

    private lateinit var recyclerView: RecyclerView

    private val adapter by lazy {
        ToDoListAdapter(
            onItemClicked = { pos, item ->
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

        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        val todoItem = TodoItem("1", "Купить что-то, где-то, зачем-то, но зачем не очень понятно, но точно чтобы показать как обр…", Importance.NORMAL, "2342", true, "sfsdf", "sfsdf")
        val todoItem1 = TodoItem("2", "Купить что-то", Importance.LOW, "234fgh2", true, "sfsfghdf", "sfsfghdf")
        val todoItems = listOf(todoItem, todoItem1, todoItem, todoItem, todoItem, todoItem, todoItem, todoItem, todoItem)
        adapter.submitList(todoItems)

        binding.addItem.setOnClickListener {
            val action = ToDoListFragmentDirections.actionToDoListFragmentToToDoFragment()
            this.findNavController().navigate(action)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}