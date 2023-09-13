package ru.kovsheful.wallcraft.domain.models

data class CollectionModel(
    val id: String,
    val title: String,
    val imageUrl: String? = null
)
