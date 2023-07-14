package com.example.todolist.presentation.settings

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todolist.R
import com.example.todolist.ToDoAppApplication
import com.example.todolist.data.database.AppDatabase
import com.example.todolist.data.network.api.TodoApiService
import com.example.todolist.databinding.FragmentSettingsBinding
import com.example.todolist.presentation.util.DataStoreManager
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private var theme = 0

    @Inject
    lateinit var dataStore: DataStore<Preferences>
    private lateinit var dataStoreManager: SettingsDataStore

    override fun onAttach(context: Context) {
        super.onAttach(context)
        (requireActivity().application as ToDoAppApplication).appComponent
            .settingsComponent()
            .create()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dataStoreManager = SettingsDataStore(dataStore)

        bindTheme()

        binding.night.setOnClickListener { toggleTheme(AppCompatDelegate.MODE_NIGHT_YES) }
        binding.light.setOnClickListener { toggleTheme(AppCompatDelegate.MODE_NIGHT_NO) }
        binding.system.setOnClickListener { toggleTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) }

        binding.back.setOnClickListener { findNavController().navigateUp() }

    }

    private fun bindTheme(){
        lifecycleScope.launch {
            theme = dataStoreManager.readTheme()
            when(theme) {
                AppCompatDelegate.MODE_NIGHT_YES ->  binding.night.isChecked = true
                AppCompatDelegate.MODE_NIGHT_NO ->  binding.light.isChecked = true
                else -> binding.system.isChecked = true
            }
        }
    }

    private fun toggleTheme(theme: Int){
        lifecycleScope.launch {
            dataStoreManager.saveTheme(theme)
//            requireActivity().recreate()
            AppCompatDelegate.setDefaultNightMode(theme)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}