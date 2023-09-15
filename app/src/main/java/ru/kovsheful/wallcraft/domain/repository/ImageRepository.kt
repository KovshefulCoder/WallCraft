package ru.kovsheful.wallcraft.domain.repository

interface ImageRepository {
    suspend fun getHighQualityImageUrl(imageID: Int): String
}