package ru.kovsheful.wallcraft.data.repository

import ru.kovsheful.wallcraft.data.settingsManager.SettingsManager
import ru.kovsheful.wallcraft.domain.models.SettingsModel
import ru.kovsheful.wallcraft.domain.repository.SettingsRepository
import ru.kovsheful.wallcraft.domain.repository.SettingsType
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsManager: SettingsManager
): SettingsRepository {
    override suspend fun getSettings() = SettingsModel (
        isDarkTheme = (settingsManager.getTheme() == "dark"),
        numberOfCollections = settingsManager.getNumberOfCollections(),
        imagesInCollection = settingsManager.getNumberImagesInCollection()
    )

    override suspend fun updateSettings(event: SettingsType) {
        when(event) {
            is SettingsType.Theme -> {
                settingsManager.updateTheme(event.isDarkTheme)
            }
            is SettingsType.NumberOfCollections -> {
                settingsManager.updateNumberOfCollections(event.number)
            }
            is SettingsType.ImagesInCollection -> {
                settingsManager.updateNumberImagesInCollection(event.number)
            }
        }
    }
}