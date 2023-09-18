package ru.kovsheful.wallcraft.data.local

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kovsheful.wallcraft.domain.models.ImageModel

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey
    val id: Int,
    val url: String,
    val isFavorite: Boolean = false,
    val localUri: String? = null
) {
    fun toImageModel() = ImageModel(
        id = id,
        url = localUri ?: url // if there are downloaded local version.
    )
}
