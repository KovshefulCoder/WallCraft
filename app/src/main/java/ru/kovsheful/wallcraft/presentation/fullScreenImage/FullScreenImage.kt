package ru.kovsheful.wallcraft.presentation.fullScreenImage

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
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import ru.kovsheful.wallcraft.R
import ru.kovsheful.wallcraft.core.Screens
import ru.kovsheful.wallcraft.core.SharedViewModelEvents
import ru.kovsheful.wallcraft.ui.theme.DropDownMenuColor
import ru.kovsheful.wallcraft.ui.theme.SecondaryText
import ru.kovsheful.wallcraft.ui.theme.TextColor
import ru.kovsheful.wallcraft.ui.theme.typography


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
    val state by viewModel.state.collectAsStateWithLifecycle()

    val viewModelEvent by viewModel.event.collectAsStateWithLifecycle(initialValue = SharedViewModelEvents.None)
    val context = LocalContext.current
    LaunchedEffect(viewModelEvent) {
        when (val event = viewModelEvent) {
            is SharedViewModelEvents.None -> {}
            is SharedViewModelEvents.OnShowToast -> {
                Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
            }
        }
    }
    FullScreenImage(imageUrl = state.highQualityImageUrl)
}

@Composable
private fun FullScreenImage(
    imageUrl: String,
    onSetAsWallpaper: () -> Unit = {},
    onDownload: () -> Unit = {},
    onAssToFavorite: () -> Unit = {}
) {
    val isImageLoading = remember { mutableStateOf(false) }
    val isErrorLoading = remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (isImageLoading.value) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CircularProgressIndicator(
                    color = TextColor,
                    modifier = Modifier.size(50.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.full_screen_image_loading_placeholder_text),
                    textAlign = TextAlign.Center,
                    style = typography.titleSmall,
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
                    .size(200.dp)
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
                isErrorLoading.value = true
//                //Was unable to fix that error with standard googling
//                //Throes error in onError, but still often manages to display image
//                if ((error.result.throwable.message?.trim() ?: "")
//                    != "Unable to create a fetcher that supports:"
//                ) {
//                    isErrorLoading.value = true
//                }
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
                    color = DropDownMenuColor,
                    shape = RoundedCornerShape(5.dp)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ActionButton(
                title = stringResource(R.string.set_as_wallpaper_button_title),
                icon = Icons.Default.Check,
                iconColor = Color.Green,
                onClick = onSetAsWallpaper
            )
            ActionButton(
                title = stringResource(R.string.download_button_title),
                icon = ImageVector.vectorResource(R.drawable.ic_download),
                iconColor = Color.White,
                onClick = onDownload
            )
            ActionButton(
                title = stringResource(R.string.add_to_favorite_button_title),
                icon = Icons.Default.Favorite,
                iconColor = Color.Red,
                onClick = onAssToFavorite
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
                style = typography.labelLarge,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        }
    }
}

sealed interface FullScreenImageEvent {
    data class OnLoadImageInHighQuality(val imageID: Int) : FullScreenImageEvent
}