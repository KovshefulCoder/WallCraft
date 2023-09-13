package ru.kovsheful.wallcraft.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.kovsheful.wallcraft.core.Screens
import ru.kovsheful.wallcraft.ui.theme.Background
import ru.kovsheful.wallcraft.ui.theme.PrimaryColor
import ru.kovsheful.wallcraft.ui.theme.SecondaryText
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

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
private fun MainScreen(a: String) {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) {

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
                modifier = Modifier.background(SecondaryText),
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
