package ru.kovsheful.wallcraft.presentation.collectionImages

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import ru.kovsheful.wallcraft.R
import ru.kovsheful.wallcraft.core.Screens
import ru.kovsheful.wallcraft.core.SharedToastLogic
import ru.kovsheful.wallcraft.core.SharedViewModelEvents
import ru.kovsheful.wallcraft.core.TopBarEvents
import ru.kovsheful.wallcraft.core.WallCraftScaffoldNColumn
import ru.kovsheful.wallcraft.domain.models.ImageModel

const val COLLECTION_ID = "id"
const val COLLECTION_ENCODED_TITLE = "title"

fun NavGraphBuilder.collectionImages(
    navigateBack: () -> Unit,
    navigateToFullScreenImage: (Int) -> Unit,
    onSettings: () -> Unit,
    onFavorite: () -> Unit,
    onDownloads: () -> Unit
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
    ) { navEntry ->
        val collectionID = navEntry.arguments?.getString(COLLECTION_ID) ?: ""
        val collectionEncodedTitle = navEntry.arguments?.getString(COLLECTION_ENCODED_TITLE) ?: ""
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
            onImageClicked = navigateToFullScreenImage,
            onTopBarEvent = { topBarEvent ->
                when (topBarEvent) {
                    TopBarEvents.OnSettings -> onSettings()
                    TopBarEvents.OnFavorite -> onFavorite()
                    TopBarEvents.OnDownloads -> onDownloads()
                    else -> navigateBack()
                }
            },
            navEntry = navEntry
        )

    }
}

@Composable
internal fun CollectionImagesScreen(
    collectionID: String,
    collectionTitle: String,
    onImageClicked: (Int) -> Unit,
    onTopBarEvent: (TopBarEvents) -> Unit,
    navEntry: NavBackStackEntry
) {
    val viewModel: CollectionImagesViewModel = hiltViewModel(navEntry)
    LaunchedEffect(Unit) {
        viewModel.onEvent(CollectionImagesScreenEvents.OnLoadImages(collectionID))
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    val viewModelEvent by viewModel.event.collectAsStateWithLifecycle(initialValue = SharedViewModelEvents.None)
    SharedToastLogic(event = viewModelEvent)

    CollectionImagesScreen(
        images = state.images,
        collectionTitle = collectionTitle,
        onImageClicked = onImageClicked,
        onTopBarEvent = onTopBarEvent

    )
}

@Composable
private fun CollectionImagesScreen(
    images: List<ImageModel>,
    collectionTitle: String,
    onImageClicked: (Int) -> Unit,
    onTopBarEvent: (TopBarEvents) -> Unit,
    ) {
    WallCraftScaffoldNColumn(
        scaffoldTitle = collectionTitle,
        subtitle = stringResource(id = R.string.collection_images_screen_subtitle),
        onTopBarEvent = onTopBarEvent
    )
    {
        if (images == listOf<ImageModel>()) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.onBackground,
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