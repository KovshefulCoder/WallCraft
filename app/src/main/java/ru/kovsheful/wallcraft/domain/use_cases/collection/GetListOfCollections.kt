package ru.kovsheful.wallcraft.domain.use_cases.collection

import ru.kovsheful.wallcraft.domain.models.CollectionModel
import ru.kovsheful.wallcraft.domain.repository.CollectionsRepository
import javax.inject.Inject

class GetListOfCollections @Inject constructor(
    private val collectionsRepository: CollectionsRepository
) {
    suspend operator fun invoke() : List<CollectionModel> {
        return collectionsRepository.getListOfCollections()
    }
}