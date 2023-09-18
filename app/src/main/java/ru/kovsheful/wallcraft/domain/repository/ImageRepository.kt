package ru.kovsheful.wallcraft.domain.repository

import ru.kovsheful.wallcraft.domain.models.ImageModel

interface ImageRepository {
    suspend fun getHighQualityImageUrl(imageID: Int): ImageModel
    suspend fun downloadImageFromUrl(image: ImageModel)
    suspend fun addImageToFavorites(image: ImageModel)
    suspend fun setImageAsWallpaper(image: ImageModel, wallpaperType: Int)
    suspend fun getDownloadedImages(): List<String>
    suspend fun getFavoriteImages(): List<String>
}