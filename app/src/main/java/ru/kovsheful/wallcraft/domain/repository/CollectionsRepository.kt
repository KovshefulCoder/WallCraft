package ru.kovsheful.wallcraft.domain.repository

import ru.kovsheful.wallcraft.domain.models.CollectionModel
import ru.kovsheful.wallcraft.domain.models.ImageModel

interface CollectionsRepository {
    suspend fun getListOfCollections(): List<CollectionModel>
    suspend fun getTitleImageUrl(id: String) : String
    suspend fun getCollectionImages(id: String) : List<ImageModel>
}