package ru.kovsheful.wallcraft.core

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun SharedToastLogic(event: SharedViewModelEvents) {
    val context = LocalContext.current
    LaunchedEffect(event) {
        when (val event = event) {
            is SharedViewModelEvents.None -> {}
            is SharedViewModelEvents.OnShowToast -> {
                Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }
        }
    }
}

sealed class SharedViewModelEvents {
    data object None: SharedViewModelEvents()
    data class OnShowToast(val message: String): SharedViewModelEvents()
}