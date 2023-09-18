package ru.kovsheful.wallcraft.data.repository

import android.app.DownloadManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.kovsheful.wallcraft.core.ImageAlreadyHaveThisStatus
import ru.kovsheful.wallcraft.data.imageManagers.ImageDownloadManager
import ru.kovsheful.wallcraft.data.imageManagers.ImageWallpaperManager
import ru.kovsheful.wallcraft.data.local.ImageEntity
import ru.kovsheful.wallcraft.data.local.ImagesDao
import ru.kovsheful.wallcraft.data.remote.ImageAPI
import ru.kovsheful.wallcraft.domain.models.ImageModel
import ru.kovsheful.wallcraft.domain.repository.ImageRepository
import ru.kovsheful.wallcraft.utils.apiCall
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageAPI: ImageAPI,
    private val imageDownloadManager: ImageDownloadManager,
    private val imageWallpaperManager: ImageWallpaperManager,
    private val imagesDao: ImagesDao
) : ImageRepository {
    override suspend fun getHighQualityImageUrl(imageID: Int): String = apiCall {
        imageAPI.getHighQualityImage(imageID).toImageEntity().highQualityUrl
    }

    override suspend fun downloadImageFromUrl(imageUrl: String, imageApiID: Int) {
        val downloadedImages = imagesDao.getLocalImages()
        if (downloadedImages.none { img -> img.id == imageApiID }) {
            imageDownloadManager.downloadImage(
                imageUrl = imageUrl,
                imageApiID = imageApiID,
                visibilityStatus = DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            )
        } else {
            throw ImageAlreadyHaveThisStatus("Image already downloaded")
        }
    }

    override suspend fun addImageToFavorites(image: ImageModel) {
        val downloadedImages = imagesDao.getLocalImages()
        if (downloadedImages.none { img -> img.id == image.id }) {
            imagesDao.upsertImage(imageModel_to_ImageEntity(image))
        } else {
            throw ImageAlreadyHaveThisStatus("Image already in favorites")
        }
    }

    override suspend fun setImageAsWallpaper(imageUrl: String, wallpaperType: Int) {
        withContext(Dispatchers.IO) {
            imageWallpaperManager.setImageAsWallpaper(imageUrl, wallpaperType)
        }
    }
}

fun imageModel_to_ImageEntity(imageModel: ImageModel) = ImageEntity(
    id = imageModel.id,
    url = imageModel.url,
)