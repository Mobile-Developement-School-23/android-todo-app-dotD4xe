package com.example.todolist.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class DataStoreManager(private val dataStore: DataStore<Preferences>) {

    private val revisionKey = intPreferencesKey("revision")

    suspend fun readRevision(): Int {
        return dataStore.data
            .map { preferences ->
                preferences[revisionKey] ?: 0
            }
            .catch { exception ->
                // Обработка ошибок чтения
            }
            .onStart {
                // Действия, выполняющиеся перед чтением из DataStore
            }
            .first()
    }

    suspend fun writeRevision(revision: Int) {
        dataStore.edit { preferences ->
            preferences[revisionKey] = revision
        }
    }
}