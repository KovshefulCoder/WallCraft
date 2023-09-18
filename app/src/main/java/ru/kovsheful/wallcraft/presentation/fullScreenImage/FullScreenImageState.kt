package ru.kovsheful.wallcraft.presentation.fullScreenImage

data class FullScreenImageState(
    val imageID: Int = 0,
    val highQualityImageUrl: String = "",
    val onLoading: Boolean = false
)