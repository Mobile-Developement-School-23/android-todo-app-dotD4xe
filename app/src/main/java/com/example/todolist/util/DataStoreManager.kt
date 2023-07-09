package com.example.todolist.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class DataStoreManager(private val dataStore: DataStore<Preferences>) {

    private val revisionKey = intPreferencesKey("revision")

    suspend fun readRevision(): Int {
        return dataStore.data
            .map { preferences ->
                preferences[revisionKey] ?: 0
            }.first()
    }

    suspend fun writeRevision(revision: Int) {
        dataStore.edit { preferences ->
            preferences[revisionKey] = revision
        }
    }
}