package ru.kovsheful.wallcraft.data.entities

import com.google.gson.annotations.SerializedName
import ru.kovsheful.wallcraft.domain.models.ImageModel

data class Media(
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
    val type: String,
    val url: String,
    val width: Int
) {
    fun toImageEntity() =
        if (type != "Photo") {
            null
        } else {
            ImageModel(
                url = src.large,
                id = id
            )
        }
}