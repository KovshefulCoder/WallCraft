package ru.kovsheful.wallcraft.presentation.collectionImages

import android.net.Uri
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import ru.kovsheful.wallcraft.R
import ru.kovsheful.wallcraft.core.Screens
import ru.kovsheful.wallcraft.core.WallCraftScaffoldNColumn
import ru.kovsheful.wallcraft.domain.models.CollectionModel
import ru.kovsheful.wallcraft.domain.models.ImageModel
import ru.kovsheful.wallcraft.presentation.home.CategoryGridItem
import ru.kovsheful.wallcraft.ui.theme.TextColor

const val COLLECTION_ID = "id"
const val COLLECTION_ENCODED_TITLE = "title"

fun NavGraphBuilder.collectionImages(
    navigateBack: () -> Unit,
    navigateToFullScreenImage: (Int) -> Unit
) {
    composable(
        route = Screens.CollectionImages.route + "/{$COLLECTION_ID}/{$COLLECTION_ENCODED_TITLE}",
        arguments = listOf(navArgument(COLLECTION_ID) {
            type = NavType.StringType
        }, navArgument(COLLECTION_ENCODED_TITLE) {
            type = NavType.StringType
            defaultValue = "none"
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
        val collectionID = entry.arguments?.getString(COLLECTION_ID) ?: ""
        val collectionEncodedTitle = entry.arguments?.getString(COLLECTION_ENCODED_TITLE) ?: ""
        if (collectionID.isEmpty() || collectionEncodedTitle.isEmpty() || collectionEncodedTitle == "none") {
            Log.i(
                "collectionImagesScreen",
                "Null on collection id or title in collectionImages composable"
            )
            navigateBack()
        }
        BackHandler {
            navigateBack()
        }
        CollectionImagesScreen(
            collectionID = collectionID,
            collectionTitle = Uri.decode(collectionEncodedTitle),
            onImageClicked = navigateToFullScreenImage
        )

    }
}

@Composable
internal fun CollectionImagesScreen(
    collectionID: String,
    collectionTitle: String,
    onImageClicked: (Int) -> Unit
) {
    val viewModel: CollectionImagesViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        viewModel.onEvent(CollectionImagesScreenEvents.OnLoadImages(collectionID))
    }
    val state by viewModel.state.collectAsStateWithLifecycle()
    CollectionImagesScreen(
        images = state.images,
        collectionTitle = collectionTitle,
        onImageClicked = onImageClicked

    )
}

@Composable
private fun CollectionImagesScreen(
    images: List<ImageModel>,
    collectionTitle: String,
    onImageClicked: (Int) -> Unit
) {
    WallCraftScaffoldNColumn(
        scaffoldTitle = collectionTitle,
        subtitle = stringResource(id = R.string.collection_images_screen_subtitle)
    )
    {
        if (images == listOf<ImageModel>()) {
            CircularProgressIndicator(
                color = TextColor,
                modifier = Modifier
                    .padding(vertical = 100.dp)
                    .size(50.dp)
            )
        }
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Adaptive(minSize = 160.dp),
            verticalItemSpacing = 4.dp,
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            items(images, key = { image ->
                image.id
            }) { image ->
                AsyncImage(
                    model = image.url,
                    contentDescription = "Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable {
                            onImageClicked(image.id)
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


sealed interface CollectionImagesScreenEvents {
    data class OnLoadImages(val id: String) : CollectionImagesScreenEvents
}