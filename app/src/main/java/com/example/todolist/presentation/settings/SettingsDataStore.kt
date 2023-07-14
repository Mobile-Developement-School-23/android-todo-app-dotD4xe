package com.example.todolist.presentation.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class SettingsDataStore(private val dataStore: DataStore<Preferences>) {

    private val themeKey = intPreferencesKey("theme")

    suspend fun saveTheme(theme: Int) {
        dataStore.edit { preferences ->
            preferences[themeKey] = theme
        }
    }

    suspend fun readTheme(): Int {
        return dataStore.data
            .map { preferences ->
                preferences[themeKey] ?: -1
            }.first()
    }
}