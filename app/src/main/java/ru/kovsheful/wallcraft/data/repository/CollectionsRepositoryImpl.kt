package ru.kovsheful.wallcraft.data.repository

import ru.kovsheful.wallcraft.data.remote.CollectionsAPI
import ru.kovsheful.wallcraft.domain.models.CollectionModel
import ru.kovsheful.wallcraft.domain.models.ImageModel
import ru.kovsheful.wallcraft.domain.repository.CollectionsRepository
import ru.kovsheful.wallcraft.utils.apiCall
import javax.inject.Inject

class CollectionsRepositoryImpl @Inject constructor(
    private val collectionsAPI: CollectionsAPI
) : CollectionsRepository {
    override suspend fun getListOfCollections(): List<CollectionModel> = apiCall {
        collectionsAPI.getListOfCollections().collections.map { collectionEntity ->
            collectionEntity.toCollectionsModel()
        }
    }

    override suspend fun getTitleImageUrl(id: String): String = apiCall {
        collectionsAPI.getCollectionTitleImageById(id).media[0].src.medium
    }

    override suspend fun getCollectionImages(id: String): List<ImageModel> = apiCall {
        collectionsAPI.getCollectionImages(id).media.mapNotNull { media ->
            media.toImageEntity()
        }
    }
}