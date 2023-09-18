package ru.kovsheful.wallcraft.presentation.filteredImages

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
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
import ru.kovsheful.wallcraft.core.WallCraftAdditionalScreenWithScaffold
import ru.kovsheful.wallcraft.domain.models.CollectionModel
import ru.kovsheful.wallcraft.domain.models.ImageModel
import ru.kovsheful.wallcraft.presentation.fullScreenImage.FullScreenImageEvent
import ru.kovsheful.wallcraft.presentation.fullScreenImage.FullScreenImageViewModel

internal const val DOWNLOADED = "Downloaded"
internal const val FAVORITE = "Favorite"
internal const val SCREEN_ID = "screen"

fun NavGraphBuilder.filteredImages(
    navigateBack: () -> Unit,
) {
    composable(
        route = Screens.FilteredImages.route + "/{$SCREEN_ID}",
        arguments = listOf(navArgument(SCREEN_ID) {
            type = NavType.StringType
        }),
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Down,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Companion.Up,
                animationSpec = tween(300)
            )
        },
    ) { navEntry ->
        val screenID = navEntry.arguments?.getString(SCREEN_ID) ?: ""
        if (screenID.isEmpty()) {
            Log.i(
                "collectionImagesScreen",
                "Null on image id in fullScreenImage composable"
            )
            navigateBack()
        }
        BackHandler {
            navigateBack()
        }
        FilteredImages(
            navigateBack = navigateBack,
            screenID = screenID,
            navEntry = navEntry
        )
    }
}

@Composable
internal fun FilteredImages(
    navigateBack: () -> Unit,
    screenID: String,
    navEntry: NavBackStackEntry
) {
    val viewModel: FilteredImagesViewModel = hiltViewModel(navEntry)
    LaunchedEffect(Unit) {
        viewModel.onEvent(FilteredImagesScreenEvent.OnLoadImagesByFilter(screenID))
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    val viewModelEvent by viewModel.event.collectAsStateWithLifecycle(initialValue = SharedViewModelEvents.None)
    SharedToastLogic(event = viewModelEvent)
    FilteredImages(
        images = state.imagesLinks,
        screenName = screenID,
        navigateBack = navigateBack,
    )
}

@Composable
private fun FilteredImages(
    images: List<String>,
    screenName: String,
    navigateBack: () -> Unit,
) {
    WallCraftAdditionalScreenWithScaffold(
        scaffoldTitle = screenName + stringResource(id = R.string.filtered_images_screen_title_postfix),
        onReturn = navigateBack,
    ) {
        if (images == listOf<String>()) {
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
            modifier = Modifier.fillMaxSize()
        ) {
            items(images) { imageLink ->
                AsyncImage(
                    model = imageLink,
                    contentDescription = null,
                    modifier  = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .clickable {  },
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


sealed interface FilteredImagesScreenEvent {
    data class OnLoadImagesByFilter(val filter: String) : FilteredImagesScreenEvent
}