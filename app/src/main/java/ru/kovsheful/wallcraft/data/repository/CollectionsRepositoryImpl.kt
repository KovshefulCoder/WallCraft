package ru.kovsheful.wallcraft.data.repository

import retrofit2.HttpException
import ru.kovsheful.wallcraft.core.ConnectionTimedOut
import ru.kovsheful.wallcraft.core.UnknownError
import ru.kovsheful.wallcraft.data.remote.CollectionsAPI
import ru.kovsheful.wallcraft.domain.models.CollectionModel
import ru.kovsheful.wallcraft.domain.repository.CollectionsRepository
import javax.inject.Inject

class CollectionsRepositoryImpl @Inject constructor(
    private val collectionsAPI: CollectionsAPI
) : CollectionsRepository {
    override suspend fun getListOfCollections(): List<CollectionModel> {
        return try {
            collectionsAPI.getListOfCollections().collections.map { collectionEntity ->
                collectionEntity.toCollectionsModel()
            }
        } catch(e: HttpException) {
            when (e.code()) {
                522 -> throw ConnectionTimedOut("522")
                else -> throw UnknownError(e.code().toString())
            }
        }
    }


    override suspend fun getTitleImageUrl(id: String): String =
        collectionsAPI.getCollectionTitleImageById(id).media[0].src.medium
}