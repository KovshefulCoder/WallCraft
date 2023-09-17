package ru.kovsheful.wallcraft.presentation.home

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import ru.kovsheful.wallcraft.R
import ru.kovsheful.wallcraft.core.Screens
import ru.kovsheful.wallcraft.core.SharedToastLogic
import ru.kovsheful.wallcraft.core.SharedViewModelEvents
import ru.kovsheful.wallcraft.core.WallCraftScaffoldNColumn
import ru.kovsheful.wallcraft.domain.models.CollectionModel
import ru.kovsheful.wallcraft.ui.theme.WallCraftCleanArchitectureTheme


fun NavGraphBuilder.home(
    onCollectionClicked: (String, String) -> Unit
) {
    composable(
        route = Screens.Home.route,
        enterTransition = {
            fadeIn(animationSpec = tween(300))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(300))
        }
    ) {navEntry ->
        MainScreen(
            onCollectionClicked = onCollectionClicked,
            navEntry = navEntry
        )
    }
}


@Composable
internal fun MainScreen(
    onCollectionClicked: (String, String) -> Unit,
    navEntry: NavBackStackEntry
) {
    val viewModel: HomeViewModel = hiltViewModel(navEntry)
    val state by viewModel.state.collectAsStateWithLifecycle()
    val viewModelEvent by viewModel.event.collectAsStateWithLifecycle(initialValue = SharedViewModelEvents.None)
    LaunchedEffect(Unit) {
        viewModel.onEvent(HomeScreenEvents.OnLoadCollections)
    }
    SharedToastLogic(event = viewModelEvent)
    MainScreen(
        collections = state.collections,
        onCollectionClicked = { selectedID ->
            val encodedCollectionTile = Uri.encode(state.collections.find { collection ->
                collection.id == selectedID
            }?.title ?: "none")
            onCollectionClicked(selectedID, encodedCollectionTile)
        }
    )
}

@Composable
private fun MainScreen(
    collections: List<CollectionModel>,
    onCollectionClicked: (String) -> Unit
) {
    WallCraftScaffoldNColumn(
        scaffoldTitle = stringResource(R.string.home_screen_title),
        subtitle = stringResource(id = R.string.home_screen_subtitle)
    )
    {
        if (collections == listOf<CollectionModel>()) {
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
            items(collections, key = { collection ->
                collection.id
            }) { collection ->
                CategoryGridItem(
                    url = collection.imageUrl,
                    title = collection.title,
                    onCollectionClicked = {
                        onCollectionClicked(collection.id)
                    }
                )
            }
        }
    }
}

@Composable
fun CategoryGridItem(
    url: String?,
    title: String,
    onCollectionClicked: () -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .clickable { onCollectionClicked() }
    ) {
        AsyncImage(
            model = url ?: R.drawable.error_image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(5.dp)
                ),
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 32.dp),
                style = MaterialTheme.typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = MaterialTheme.colorScheme.onSecondary,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

sealed class HomeScreenEvents {
    data object OnLoadCollections : HomeScreenEvents()
}