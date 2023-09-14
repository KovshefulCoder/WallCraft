package ru.kovsheful.wallcraft.presentation.collectionImages

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.kovsheful.wallcraft.core.Screens
import ru.kovsheful.wallcraft.domain.models.CollectionModel
import ru.kovsheful.wallcraft.domain.models.ImageModel

const val COLLECTION_ID = "id"

fun NavGraphBuilder.collectionImages(
    navigateBack: () -> Unit
) {
    composable(
        route = Screens.CollectionImages.route,
        arguments = listOf(navArgument(COLLECTION_ID) {
            type = NavType.StringType
        }),
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                animationSpec = tween(300)
            )
        },
    ) {entry ->
        val collectionID = entry.arguments?.getString(COLLECTION_ID) ?: ""
        if (collectionID.isEmpty()){
            Log.i("collectionImagesScreen", "Null on collection id on collectionImages screen")
            navigateBack()
        }
        BackHandler {
            navigateBack()
        }
        CollectionImagesScreen(
            collectionID = collectionID
        )

    }
}

@Composable
internal fun CollectionImagesScreen(
    collectionID: String
) {
    val viewModel: CollectionImagesViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.onEvent(CollectionImagesScreenEvents.OnLoadImages(collectionID))
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    CollectionImagesScreen(
        images = state.images
    )
}

@Composable
private fun CollectionImagesScreen(
    images: List<ImageModel>
) {

}


sealed interface CollectionImagesScreenEvents {
    data class OnLoadImages(val id: String): CollectionImagesScreenEvents
}