package ru.kovsheful.wallcraft.presentation.home

import android.util.Log
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.AsyncImage
import ru.kovsheful.wallcraft.R
import ru.kovsheful.wallcraft.core.Screens
import ru.kovsheful.wallcraft.ui.theme.Background
import ru.kovsheful.wallcraft.ui.theme.PrimaryColor
import ru.kovsheful.wallcraft.ui.theme.TextColor
import ru.kovsheful.wallcraft.ui.theme.TopBarColor
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
    MainScreen("")
}

@Preview
@Composable
fun PrevMainScreen() {
    MainScreen("")
}

@Composable
private fun MainScreen(a: String) {
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
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Adaptive(minSize = 160.dp),
                verticalItemSpacing = 4.dp,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                items(5) {
                    CategoryGridItem(
                        url = "https://upload.wikimedia.org/wikipedia/en/a/a9/Example.jpg",
                        title = "Title",
                        onCategoryClicked = {}
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryGridItem(
    url: String,
    title: String,
    onCategoryClicked: (Long) -> Unit
) {
    val isLoading = remember { mutableStateOf(false) }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .clickable { onCategoryClicked(0) } //TODO fix
    ) {
        if (isLoading.value) {
            CircularProgressIndicator(
                color = TextColor,
                modifier = Modifier.padding(vertical = 100.dp)
            )
        }
        AsyncImage(
            model = url,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .background(
                    color = PrimaryColor.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(5.dp)
                ),
        ) {
            Text(
                text = if (isLoading.value)
                    stringResource(R.string.categories_loading_status_title)
                else title,
                modifier = Modifier.padding(vertical = 4.dp, horizontal = 32.dp),
                style = typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun WallCraftTopBar(
    title: String
) {
    val dropDownMenuExpanded = remember {
        mutableStateOf(false)
    }
    TopAppBar(
        title = {
            Text(
                text = title,
                style = typography.titleLarge
            )
        },
        actions = {
            IconButton(
                onClick = { dropDownMenuExpanded.value = true }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_vertical_dots),
                    contentDescription = "Dropdown menu"
                )
            }
            // drop down menu
            DropdownMenu(
                expanded = dropDownMenuExpanded.value,
                onDismissRequest = {
                    dropDownMenuExpanded.value = false
                },
                offset = DpOffset(x = 10.dp, y = (-60).dp)
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.dropdown_menu_settings),
                            style = typography.bodySmall
                        )
                    },
                    onClick = {
                        dropDownMenuExpanded.value = false
                    }
                )
                Divider()
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.dropdown_menu_favorite),
                            style = typography.bodySmall
                        )
                    },
                    onClick = {
                        dropDownMenuExpanded.value = false
                    }
                )
                Divider()
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(R.string.dropdown_menu_downloads),
                            style = typography.bodySmall
                        )
                    },
                    onClick = {
                        dropDownMenuExpanded.value = false
                    }
                )
            }
        }
    )
}