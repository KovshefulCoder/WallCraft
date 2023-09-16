package ru.kovsheful.wallcraft.domain.use_cases

import ru.kovsheful.wallcraft.domain.repository.ImageRepository
import javax.inject.Inject

class SetImageAsWallpaper @Inject constructor(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(url: String) {
        return imageRepository.setImageAsWallpaper(url)
    }
}