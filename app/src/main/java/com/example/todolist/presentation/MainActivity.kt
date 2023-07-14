package com.example.todolist.presentation

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.example.todolist.R
import com.example.todolist.ToDoAppApplication
import com.example.todolist.databinding.ActivityMainBinding
import com.example.todolist.presentation.settings.SettingsDataStore
import com.example.todolist.presentation.util.NetworkStateReceiver
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * MainActivity class serves as the entry point of the application and hosts the navigation graph.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    @Inject
    lateinit var networkStateReceiver: NetworkStateReceiver

    @Inject
    lateinit var dataStore: DataStore<Preferences>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val appComponent = (application as ToDoAppApplication).appComponent
        appComponent.inject(this)

        val dataStoreManager = SettingsDataStore(dataStore)

        lifecycleScope.launch {
            coroutineScope {
                val theme = dataStoreManager.readTheme()
                AppCompatDelegate.setDefaultNightMode(theme)
            }
        }

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
    }

    override fun onStart() {
        super.onStart()
        networkStateReceiver.register()
    }

    override fun onDestroy() {
        super.onDestroy()
        networkStateReceiver.unregister()
    }
}
