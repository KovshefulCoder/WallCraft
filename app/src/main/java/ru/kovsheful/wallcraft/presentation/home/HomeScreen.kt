package ru.kovsheful.wallcraft.presentation.home

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import ru.kovsheful.wallcraft.R
import ru.kovsheful.wallcraft.core.Screens
import ru.kovsheful.wallcraft.core.WallCraftTopBar
import ru.kovsheful.wallcraft.domain.models.CollectionModel
import ru.kovsheful.wallcraft.ui.theme.Background
import ru.kovsheful.wallcraft.ui.theme.SecondaryText
import ru.kovsheful.wallcraft.ui.theme.TextColor
import ru.kovsheful.wallcraft.ui.theme.typography


fun NavGraphBuilder.home() {
    composable(
        route = Screens.Home.route,
    ) {
        MainScreen()
    }
}


@Composable
internal fun MainScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    val viewModelEvent by viewModel.event.collectAsStateWithLifecycle(initialValue = SharedViewModelEvents.None)
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.onEvent(HomeScreenEvents.OnLoadCollections)
    }
    LaunchedEffect(viewModelEvent) {
        when (val event = viewModelEvent) {
            is SharedViewModelEvents.None -> {}
            is SharedViewModelEvents.OnShowToast -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
    MainScreen(
        collections = state.collections
    )
}

@Composable
private fun MainScreen(
    collections: List<CollectionModel>
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        backgroundColor = Background,
        topBar = {
            WallCraftTopBar(
                title = stringResource(R.string.home_screen_title)
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(R.string.home_screen_subtitle),
                    style = typography.headlineMedium
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            if (collections == listOf<CollectionModel>()) {
                CircularProgressIndicator(
                    color = TextColor,
                    modifier = Modifier.padding(vertical = 100.dp).size(50.dp)
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
                items(collections) { collection ->
                    CategoryGridItem(
                        url = collection.imageUrl,
                        title = collection.title,
                        onCategoryClicked = {}
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryGridItem(
    url: String?,
    title: String,
    onCategoryClicked: (Long) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .clickable { onCategoryClicked(0) } //TODO fix
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
                    color = SecondaryText.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(5.dp)
                ),
        ) {
            Text(
                text = title,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 32.dp),
                style = typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

sealed class HomeScreenEvents {
    data object OnLoadCollections : HomeScreenEvents()
}