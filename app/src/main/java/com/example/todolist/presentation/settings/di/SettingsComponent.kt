package com.example.todolist.presentation.settings.di

import com.example.todolist.di.FragmentScope
import com.example.todolist.presentation.settings.SettingsFragment
import dagger.Subcomponent

@FragmentScope
@Subcomponent
interface SettingsComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): SettingsComponent
    }

    fun inject(fragment: SettingsFragment)
}