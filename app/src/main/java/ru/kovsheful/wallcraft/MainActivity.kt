package ru.kovsheful.wallcraft

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.kovsheful.wallcraft.core.Screens
import ru.kovsheful.wallcraft.presentation.collectionImages.collectionImages
import ru.kovsheful.wallcraft.presentation.filteredImages.DOWNLOADED
import ru.kovsheful.wallcraft.presentation.filteredImages.FAVORITE
import ru.kovsheful.wallcraft.presentation.filteredImages.filteredImages
import ru.kovsheful.wallcraft.presentation.fullScreenImage.fullScreenImage
import ru.kovsheful.wallcraft.presentation.home.home
import ru.kovsheful.wallcraft.presentation.settings.navigateToSetting
import ru.kovsheful.wallcraft.presentation.settings.settings
import ru.kovsheful.wallcraft.ui.theme.WallCraftCleanArchitectureTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPref = this.baseContext.getSharedPreferences("settings", Context.MODE_PRIVATE)
        setContent {
            WallCraftCleanArchitectureTheme(
                isDarkTheme = sharedPref.getBoolean("isDarkTheme", true)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    WallCraftApp(
                        activity = this
                    )
                }
            }
        }
    }
}

@Composable
fun WallCraftApp(
    activity: ComponentActivity
) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screens.Home.route
    ) {
        home(
            onCollectionClicked = { id, encodedTitle ->
                navController.navigate(Screens.CollectionImages.route + "/$id/$encodedTitle")
            },
            onSettings = { navController.navigateToSetting() },
            onDownloads = { navController.navigate(Screens.FilteredImages.route + "/" + DOWNLOADED) },
            onFavorite = { navController.navigate(Screens.FilteredImages.route + "/" +FAVORITE) }
        )
        collectionImages(
            navigateBack = { navController.popBackStack() },
            navigateToFullScreenImage = { imageID ->
                navController.navigate(Screens.FullScreenImage.route + "/$imageID")
            },
            onSettings = { navController.navigateToSetting() },
            onDownloads = { navController.navigate(Screens.FilteredImages.route + "/" + DOWNLOADED) },
            onFavorite = { navController.navigate(Screens.FilteredImages.route + "/" + FAVORITE) }
        )
        fullScreenImage(navigateBack = { navController.popBackStack() })
        settings(
            activity = activity,
            navigateBack = {
                navController.popBackStack()
            }
        )
        filteredImages(
            navigateBack = {
                navController.popBackStack()
            }
        )
    }
}
