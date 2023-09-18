package ru.kovsheful.wallcraft.domain.use_cases.collection

import ru.kovsheful.wallcraft.domain.models.ImageModel
import ru.kovsheful.wallcraft.domain.repository.CollectionsRepository
import javax.inject.Inject

class GetImagesOfCollection @Inject constructor(
    private val collectionsRepository: CollectionsRepository
) {
    suspend operator fun invoke(id: String) : List<ImageModel> {
        return collectionsRepository.getCollectionImages(id)
    }
}