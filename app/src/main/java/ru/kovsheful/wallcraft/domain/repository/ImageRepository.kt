package ru.kovsheful.wallcraft.domain.repository

import ru.kovsheful.wallcraft.domain.models.ImageModel

interface ImageRepository {
    suspend fun getHighQualityImageUrl(imageID: Int): String
    suspend fun downloadImageFromUrl(imageUrl: String, imageApiID: Int)
    suspend fun addImageToFavorites(image: ImageModel)
    suspend fun setImageAsWallpaper(imageUrl: String, wallpaperType: Int)
}