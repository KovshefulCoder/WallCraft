package ru.kovsheful.wallcraft.presentation.fullScreenImage

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import ru.kovsheful.wallcraft.core.Screens
import ru.kovsheful.wallcraft.presentation.collectionImages.COLLECTION_ENCODED_TITLE
import ru.kovsheful.wallcraft.presentation.collectionImages.COLLECTION_ID
import ru.kovsheful.wallcraft.presentation.collectionImages.CollectionImagesScreen


const val IMAGE_ID = "id"


fun NavGraphBuilder.fullScreenImage(
    navigateBack: () -> Unit,
) {
    composable(
        route = Screens.FullScreenImage.route + "/{$IMAGE_ID}",
        arguments = listOf(navArgument(IMAGE_ID) {
            type = NavType.IntType
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
    ) { entry ->
        val imageID = entry.arguments?.getInt(IMAGE_ID) ?: 0
        if (imageID == 0) {
            Log.i(
                "collectionImagesScreen",
                "Null on image id in fullScreenImage composable"
            )
            navigateBack()
        }
        BackHandler {
            navigateBack()
        }
        FullScreenImage(imageID)
    }
}

@Composable
internal fun FullScreenImage(
    imageID: Int
) {
    val viewModel: FullScreenImageViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.onEvent(FullScreenImageEvent.OnLoadImageInHighQuality(imageID))
    }
}

sealed interface FullScreenImageEvent {
    data class OnLoadImageInHighQuality(val imageID: Int): FullScreenImageEvent
}