package ru.kovsheful.wallcraft.domain.use_cases

import ru.kovsheful.wallcraft.domain.repository.ImageRepository
import javax.inject.Inject

class DownloadImageByUrl @Inject constructor(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(url: String) {
        return imageRepository.downloadImageFromUrl(url)
    }
}