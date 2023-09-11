package ru.kovsheful.wallcraft.core

sealed class Screens(val route: String) {
    data object Home: Screens("Categories")
    data object Category: Screens("Category")
    data object Photo: Screens("Photo")
    data object Settings : Screens("Settings")
    data object Favorite: Screens("Favorite")
    data object Downloaded: Screens("Downloaded")
}