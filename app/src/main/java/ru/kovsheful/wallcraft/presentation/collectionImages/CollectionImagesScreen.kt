package ru.kovsheful.wallcraft.presentation.collectionImages

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.kovsheful.wallcraft.core.Screens

fun NavGraphBuilder.collectionImages() {
    composable(
        route = Screens.Collection.route,
    ) {
        CollectionImagesScreen()
    }
}

@Composable
internal fun CollectionImagesScreen() {

}