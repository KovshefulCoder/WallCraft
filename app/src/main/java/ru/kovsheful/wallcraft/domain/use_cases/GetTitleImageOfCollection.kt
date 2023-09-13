package ru.kovsheful.wallcraft.domain.use_cases

import ru.kovsheful.wallcraft.domain.models.CollectionModel
import ru.kovsheful.wallcraft.domain.repository.CollectionsRepository
import javax.inject.Inject

class GetTitleImageOfCollection @Inject constructor(
    private val collectionsRepository: CollectionsRepository
) {
    suspend operator fun invoke(id: String) : String {
        return collectionsRepository.getTitleImageUrl(id)
    }
}