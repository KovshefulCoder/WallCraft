package ru.kovsheful.wallcraft.presentation.collectionImages

import ru.kovsheful.wallcraft.domain.models.ImageModel

data class CollectionImagesState (
    val title: String = "",
    val images: List<ImageModel> = listOf()
)