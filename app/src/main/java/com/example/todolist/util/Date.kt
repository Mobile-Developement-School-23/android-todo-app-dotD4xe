package com.example.todolist.util

import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker

fun buildCalendarConstraints(): CalendarConstraints {
    val constraintsBuilder = CalendarConstraints.Builder()

    val currentDate = MaterialDatePicker.todayInUtcMilliseconds()
    constraintsBuilder.setStart(currentDate)

    val pointForwardValidator = DateValidatorPointForward.from(currentDate)
    constraintsBuilder.setValidator(pointForwardValidator)

    return constraintsBuilder.build()
}