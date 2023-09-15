package ru.kovsheful.wallcraft.data.entities

import com.google.gson.annotations.SerializedName
import ru.kovsheful.wallcraft.data.entities.Src
import ru.kovsheful.wallcraft.domain.models.ImageModel

data class ImageEntity(
    val alt: String,
    @SerializedName("avg_color")
    val avgColor: String,
    val height: Int,
    val id: Int,
    val liked: Boolean,
    val photographer: String,
    @SerializedName("photographer_id")
    val photographerId: Int,
    @SerializedName("photographer_url")
    val photographerUrl: String,
    val src: Src,
    val url: String,
    val width: Int
) {
    fun toImageEntity() = ImageModel(
        id = id,
        url = src.medium,
        highQualityUrl = src.original
    )
}