package ru.kovsheful.wallcraft.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.kovsheful.wallcraft.domain.models.ImageModel

@Entity(tableName = "images")
data class ImageEntity(
    @PrimaryKey
    val id: Int,
    val url: String,
    val name: String,
    val isFavorite: Boolean = false,
    val localUri: String? = null
)
