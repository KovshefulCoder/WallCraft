package ru.kovsheful.wallcraft.domain.use_cases

import ru.kovsheful.wallcraft.domain.models.ImageModel
import ru.kovsheful.wallcraft.domain.repository.CollectionsRepository
import ru.kovsheful.wallcraft.domain.repository.ImageRepository
import javax.inject.Inject

class GetHighQualityImage @Inject constructor(
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(id: Int) : String {
        return imageRepository.getHighQualityImageUrl(id)
    }
}