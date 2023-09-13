package ru.kovsheful.wallcraft.presentation.home

import ru.kovsheful.wallcraft.domain.models.CollectionModel

data class HomeState(
   val collections: List<CollectionModel> = listOf()
)
