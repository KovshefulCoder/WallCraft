package ru.kovsheful.wallcraft.presentation.settings

data class SettingsState(
    val isDarkTheme: Boolean = true,
    val numberOfCollections: Int = 15,
    val imagesInCollection: Int = 15
)
