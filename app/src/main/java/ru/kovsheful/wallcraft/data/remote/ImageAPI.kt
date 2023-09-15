package ru.kovsheful.wallcraft.data.remote

import retrofit2.http.GET
import ru.kovsheful.wallcraft.data.entities.ImageEntity

interface ImageAPI {
    @GET("photos/{id}")
    suspend fun getHighQualityImage(imageID: Int) : ImageEntity
}