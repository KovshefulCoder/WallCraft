package ru.kovsheful.wallcraft.domain.models

data class SettingsModel(
    val isDarkTheme: Boolean = true,
    val numberOfCollections: Int = 15,
    val imagesInCollection: Int = 15
)
