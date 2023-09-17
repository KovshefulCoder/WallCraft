package ru.kovsheful.wallcraft.presentation.settings

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.kovsheful.wallcraft.core.Screens
import ru.kovsheful.wallcraft.core.SharedToastLogic
import ru.kovsheful.wallcraft.core.SharedViewModelEvents
import ru.kovsheful.wallcraft.presentation.fullScreenImage.FullScreenImageEvent
import ru.kovsheful.wallcraft.presentation.fullScreenImage.FullScreenImageViewModel

fun NavGraphBuilder.settings(
    onReturn: () -> Unit
) {
    composable(
        route = Screens.Settings.route,
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
    ) { navBackStackEntry ->
        val viewModel: SettingsViewModel = hiltViewModel(navBackStackEntry)

        BackHandler {
            onReturn()
        }
        SettingsScreen(
            onReturn = onReturn,
            navEntry = navBackStackEntry
        )
    }
}

@Composable
fun SettingsScreen(
    onReturn: () -> Unit,
    navEntry: NavBackStackEntry
) {
    val viewModel: SettingsViewModel = hiltViewModel(navEntry)
    LaunchedEffect(Unit) {
        viewModel.onEvent(SettingsScreenEvent.OnLoadSettigns)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val viewModelEvent by viewModel.event.collectAsStateWithLifecycle(initialValue = SharedViewModelEvents.None)
    SharedToastLogic(event = viewModelEvent)
    
}


sealed interface SettingsScreenEvent {
    data object OnLoadSettigns: SettingsScreenEvent
}
