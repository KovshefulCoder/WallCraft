package ru.kovsheful.wallcraft.core

sealed class SharedViewModelEvents {
    data object None: SharedViewModelEvents()
    data class OnShowToast(val message: String): SharedViewModelEvents()
}