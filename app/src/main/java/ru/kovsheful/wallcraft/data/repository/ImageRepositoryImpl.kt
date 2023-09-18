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
    override suspend fun getHighQualityImageUrl(imageID: Int): ImageModel = apiCall {
        imageAPI.getHighQualityImage(imageID).toImageModel()
    }

    override suspend fun downloadImageFromUrl(image: ImageModel) {
        val downloadedImages = imagesDao.getDownloadedImages()
        if (downloadedImages.none { downloadedImg -> downloadedImg.id == image.id }) {
            imageDownloadManager.downloadImage(
                imageUrl = image.url,
                imageApiID = image.id,
                imageName = image.name,
                visibilityStatus = DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
            )
        } else {
            throw ImageAlreadyHaveThisStatus("Image already downloaded")
        }
    }

    override suspend fun addImageToFavorites(image: ImageModel) {
        val downloadedImages = imagesDao.getDownloadedImages()
        if (downloadedImages.none { downloadedImg -> downloadedImg.id == image.id }) {
            imagesDao.upsertImage(imageModelToImageEntity(image))
        } else {
            throw ImageAlreadyHaveThisStatus("Image already in favorites")
        }
    }

    override suspend fun setImageAsWallpaper(image: ImageModel, wallpaperType: Int) {
        withContext(Dispatchers.IO) {
            imageWallpaperManager.setImageAsWallpaper(imageModelToImageEntity(image), wallpaperType)
        }
    }
}

fun imageModelToImageEntity(imageModel: ImageModel) = ImageEntity(
    id = imageModel.id,
    url = imageModel.url,
    name = imageModel.name
)