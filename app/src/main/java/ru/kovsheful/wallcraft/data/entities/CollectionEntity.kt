package ru.kovsheful.wallcraft.data.entities

import com.google.gson.annotations.SerializedName
import ru.kovsheful.wallcraft.domain.models.CollectionModel

data class CollectionEntity(
    val description: String,
    val id: String,
    @SerializedName("media_count")
    val mediaCount: Int,
    @SerializedName("photos_count")
    val photosCount: Int,
    val `private`: Boolean,
    val title: String,
    @SerializedName("videos_count")
    val videosCount: Int
) {
    fun toCollectionsModel() = CollectionModel(
        id = id,
        title = title,
    )
}