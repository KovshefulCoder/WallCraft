package ru.kovsheful.wallcraft.domain.repository

import ru.kovsheful.wallcraft.domain.models.SettingsModel

interface SettingsRepository {
    suspend fun getSettings(): SettingsModel
    suspend fun updateSettings(event: SettingsType)
}

sealed interface SettingsType {
    data class Theme(val isDarkTheme: Boolean): SettingsType
    data class NumberOfCollections(val number: Int): SettingsType
    data class ImagesInCollection(val number: Int): SettingsType
}