package ru.kovsheful.wallcraft.domain.models

data class ImageModel(
    val id: Int,
    val url: String,
    val highQualityUrl: String = "",
)