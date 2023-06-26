package com.example.todolist.util

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
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

fun String?.toDate(): Date? {
    val format = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
    return this?.let { format.parse(it) }
}

fun Date.toText(): String {
    val format = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
    return format.format(this)
}

fun <T> Flow<T>.repeatOnCreated(lifecycleOwner: LifecycleOwner) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.CREATED) {
            this@repeatOnCreated.collect()
        }
    }
}

fun <T> Flow<T>.repeatOnCreated(lifecycleOwner: LifecycleOwner, action: suspend (T) -> Unit) {
    onEach { action(it) }
        .repeatOnCreated(lifecycleOwner)
}

inline fun <reified T> Bundle.getParcelableCompat(key: String): T? = when {
    Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU -> getParcelable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getParcelable(key) as? T?
}