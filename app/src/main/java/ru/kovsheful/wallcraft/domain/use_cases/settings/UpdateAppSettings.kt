package ru.kovsheful.wallcraft.domain.use_cases.settings

import ru.kovsheful.wallcraft.domain.repository.SettingsRepository
import ru.kovsheful.wallcraft.domain.repository.SettingsType
import javax.inject.Inject

class UpdateAppSettings @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(event: SettingsType) = settingsRepository.updateSettings(event)
}