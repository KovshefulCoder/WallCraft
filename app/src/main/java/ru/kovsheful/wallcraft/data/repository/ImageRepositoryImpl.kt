package ru.kovsheful.wallcraft.data.repository

import ru.kovsheful.wallcraft.data.remote.CollectionsAPI
import ru.kovsheful.wallcraft.data.remote.ImageAPI
import ru.kovsheful.wallcraft.domain.repository.ImageRepository
import ru.kovsheful.wallcraft.utils.apiCall
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageAPI: ImageAPI
) : ImageRepository {
    override suspend fun getHighQualityImageUrl(imageID: Int): String = apiCall {
        imageAPI.getHighQualityImage(imageID).toImageEntity().highQualityUrl
    }
}