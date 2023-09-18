package ru.kovsheful.wallcraft.presentation.fullScreenImage

import ru.kovsheful.wallcraft.domain.models.ImageModel

data class FullScreenImageState(
    val image: ImageModel = ImageModel(
        id = 0,
        name = "",
        url = "",
        highQualityUrl = ""
    ),
    val onLoading: Boolean = false
)