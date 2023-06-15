package com.example.todolist.util

import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun View.hide(){
    visibility = View.GONE
}

fun Fragment.toast(msg: String?){
    Toast.makeText(requireContext(),msg, Toast.LENGTH_LONG).show()
}

fun View.show(){
    visibility = View.VISIBLE
}

// перевод текста в дату
fun String?.toDate(): Date? {
    val format = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
    return this?.let { format.parse(it) }
}

// перевод даты в текст
fun Date.toText(): String {
    val format = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
    return format.format(this)
}