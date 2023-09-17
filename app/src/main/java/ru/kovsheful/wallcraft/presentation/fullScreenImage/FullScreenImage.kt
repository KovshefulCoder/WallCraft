package ru.kovsheful.wallcraft.presentation.fullScreenImage

import android.app.WallpaperManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.DropdownMenu
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
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
    ) { navEntry ->
        val imageID = navEntry.arguments?.getInt(IMAGE_ID) ?: 0
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
        FullScreenImage(imageID, navEntry)
    }
}

@Composable
internal fun FullScreenImage(
    imageID: Int,
    navEntry: NavBackStackEntry

) {
    val viewModel: FullScreenImageViewModel = hiltViewModel(navEntry)
    LaunchedEffect(Unit) {
        viewModel.onEvent(FullScreenImageEvent.OnLoadImageInHighQuality(imageID))
    }
    val state by viewModel.state.collectAsStateWithLifecycle()

    val viewModelEvent by viewModel.event.collectAsStateWithLifecycle(initialValue = SharedViewModelEvents.None)
    SharedToastLogic(event = viewModelEvent)
    FullScreenImage(
        imageUrl = state.highQualityImageUrl,
        onLoadingInViewModel = state.onLoading,
        onEvent = viewModel::onEvent
    )
}

@Composable
private fun FullScreenImage(
    imageUrl: String,
    onLoadingInViewModel: Boolean,
    onEvent: (FullScreenImageEvent) -> Unit
) {
    val isImageLoading = remember { mutableStateOf(false) }
    val isErrorLoading = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (onLoadingInViewModel) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(50.dp)
                )
            }
        }
        if (isImageLoading.value) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.full_screen_image_loading_placeholder_text),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (isErrorLoading.value) {
            Image(
                painter = painterResource(id = R.drawable.error_image),
                contentDescription = "Error",
                modifier = Modifier
                    .fillMaxWidth()
                    .size(400.dp)
            )
        }
        AsyncImage(
            model = imageUrl,
            modifier = Modifier.fillMaxSize(),
            onLoading = {
                Log.i("TEMP", "Start loading")
                isImageLoading.value = true
            },
            onError = { error ->
                isImageLoading.value = false
                if ((error.result.throwable.message?.trim() ?: "")
                    != "Unable to create a fetcher that supports:"
                ) {
                    //Unable to fix that error with standard googling (and with not such standard too, even visited https://juejin.cn/post/7133399457945059335)
                    //Throws error in onError, but almost always manages afterwards to display image,
                    //so showing error picture doesn`t seems logic
                    //UPDATE: Added issue on Coil GitHub: https://github.com/coil-kt/coil/issues/1864
                    isErrorLoading.value = true
                }
                Log.i("TEMP", "Error loading, ${error.result.throwable.message}")
            },
            onSuccess = {
                isImageLoading.value = false
                isErrorLoading.value = false
            },
            contentDescription = "Image"
        )
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .background(
                    color = MaterialTheme.colorScheme.secondary,
                    shape = RoundedCornerShape(5.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val dropDownMenuExpanded = remember {
                mutableStateOf(false)
            }
            ActionButton(
                title = stringResource(R.string.set_as_wallpaper_button_title),
                icon = Icons.Default.Check,
                iconColor = Color.Green,
                onClick = { dropDownMenuExpanded.value = true }
            )
            WallpaperDropDownMenu(
                dropDownMenuStatus = dropDownMenuExpanded.value,
                onDropDownHide = { dropDownMenuExpanded.value = false },
                onEvent = onEvent
            )
            ActionButton(
                title = stringResource(R.string.download_button_title),
                icon = ImageVector.vectorResource(R.drawable.ic_download),
                iconColor = Color.White,
                onClick = { onEvent(FullScreenImageEvent.OnDownloadImage) }
            )
            ActionButton(
                title = stringResource(R.string.add_to_favorite_button_title),
                icon = Icons.Default.Favorite,
                iconColor = Color.Red,
                onClick = {}
            )

        }
    }
}

@Composable
fun ActionButton(
    title: String,
    icon: ImageVector,
    iconColor: Color,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
        elevation = null
    ) {
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onBackground,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

@Composable
fun WallpaperDropDownMenu(
    dropDownMenuStatus: Boolean,
    onDropDownHide: () -> Unit,
    onEvent: (FullScreenImageEvent) -> Unit
) {
    DropdownMenu(
        expanded = dropDownMenuStatus,
        onDismissRequest = onDropDownHide,
        modifier = Modifier.background(MaterialTheme.colorScheme.secondary),
    ) {
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.dropdown_menu_wallpaper_all_screens),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            },
            onClick = {
                onDropDownHide()
                onEvent(FullScreenImageEvent.OnSetAsWallpaper())
            }
        )
        Divider()
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.dropdown_menu_wallpaper_lock_screen),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            },
            onClick = {
                onDropDownHide()
                onEvent(FullScreenImageEvent.OnSetAsWallpaper(WallpaperManager.FLAG_LOCK))
            }
        )
        Divider()
        DropdownMenuItem(
            text = {
                Text(
                    text = stringResource(R.string.dropdown_menu_wallpaper_desktop),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSecondary,
                )
            },
            onClick = {
                onDropDownHide()
                onEvent(FullScreenImageEvent.OnSetAsWallpaper(WallpaperManager.FLAG_SYSTEM))
            }
        )
    }
}

sealed interface FullScreenImageEvent {
    data class OnLoadImageInHighQuality(val imageID: Int) : FullScreenImageEvent
    data class OnSetAsWallpaper(val wallpaperType: Int = 0) : FullScreenImageEvent
    data object OnDownloadImage : FullScreenImageEvent
}