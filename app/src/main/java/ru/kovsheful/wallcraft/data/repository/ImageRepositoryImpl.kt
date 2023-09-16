package ru.kovsheful.wallcraft.data.repository

import android.app.DownloadManager
import android.app.WallpaperManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import ru.kovsheful.wallcraft.data.imageManagers.ImageDownloadManager
import ru.kovsheful.wallcraft.data.imageManagers.ImageWallpaperManager
import ru.kovsheful.wallcraft.data.remote.ImageAPI
import ru.kovsheful.wallcraft.domain.repository.ImageRepository
import ru.kovsheful.wallcraft.utils.apiCall
import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageAPI: ImageAPI,
    private val imageDownloadManager: ImageDownloadManager,
    private val imageWallpaperManager: ImageWallpaperManager
) : ImageRepository {
    override suspend fun getHighQualityImageUrl(imageID: Int): String = apiCall {
        imageAPI.getHighQualityImage(imageID).toImageEntity().highQualityUrl
    }

    override suspend fun downloadImageFromUrl(imageUrl: String) {

    }

    override suspend fun setImageAsWallpaper(imageUrl: String, wallpaperType: Int) {
        withContext(Dispatchers.IO) {
            imageWallpaperManager.setImageAsWallpaper(imageUrl, wallpaperType)
        }
    }
}