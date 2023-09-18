package ru.kovsheful.wallcraft.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import ru.kovsheful.wallcraft.data.entities.ImageDto

interface ImageAPI {
    @GET("photos/{id}")
    suspend fun getHighQualityImage(@Path("id") imageID: Int) : ImageDto
}