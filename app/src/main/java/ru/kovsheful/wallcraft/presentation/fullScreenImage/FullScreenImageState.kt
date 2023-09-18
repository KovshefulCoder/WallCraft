package ru.kovsheful.wallcraft.presentation.fullScreenImage

import ru.kovsheful.wallcraft.domain.models.ImageModel

data class FullScreenImageState(
    val image: ImageModel = ImageModel(),
    val onLoading: Boolean = false
)