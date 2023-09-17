package ru.kovsheful.wallcraft.domain.use_cases

import ru.kovsheful.wallcraft.domain.models.SettingsModel
import ru.kovsheful.wallcraft.domain.repository.SettingsRepository
import javax.inject.Inject

class GetAppSettings @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke() = settingsRepository.getSettings()
}