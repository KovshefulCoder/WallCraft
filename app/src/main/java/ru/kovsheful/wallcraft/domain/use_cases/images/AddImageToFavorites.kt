package ru.kovsheful.wallcraft.domain.use_cases.images

import ru.kovsheful.wallcraft.domain.models.ImageModel
import ru.kovsheful.wallcraft.domain.repository.ImageRepository
import javax.inject.Inject

class AddImageToFavorites @Inject constructor(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(image: ImageModel) {
        return imageRepository.addImageToFavorites(image)
    }
}