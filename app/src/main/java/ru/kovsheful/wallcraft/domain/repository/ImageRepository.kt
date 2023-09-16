package ru.kovsheful.wallcraft.domain.repository

interface ImageRepository {
    suspend fun getHighQualityImageUrl(imageID: Int): String
    suspend fun downloadImageFromUrl(imageUrl: String)
    suspend fun setImageAsWallpaper(imageUrl: String)
}