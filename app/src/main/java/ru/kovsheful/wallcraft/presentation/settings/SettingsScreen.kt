package ru.kovsheful.wallcraft.presentation.settings

//import androidx.compose.material3.SwitchDefaults.colors
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults.colors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import ru.kovsheful.wallcraft.R
import ru.kovsheful.wallcraft.core.Screens
import ru.kovsheful.wallcraft.core.SharedToastLogic
import ru.kovsheful.wallcraft.core.SharedViewModelEvents
import ru.kovsheful.wallcraft.core.WallCraftAdditionalScreenWithScaffold

fun NavController.navigateToSetting()
{
    this.navigate(
        route = Screens.Settings.route
    )
}

fun NavGraphBuilder.settings(
    activity: ComponentActivity,
    navigateBack: () -> Unit
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
        BackHandler {
            navigateBack()
        }
        SettingsScreen(
            onReturn = { navigateBack() },
            navEntry = navBackStackEntry,
            recreateActivity = {
                activity.recreate()
            }
        )
    }
}

@Composable
internal fun SettingsScreen(
    onReturn: () -> Unit,
    navEntry: NavBackStackEntry,
    recreateActivity: () -> Unit
) {
    val viewModel: SettingsViewModel = hiltViewModel(navEntry)
    LaunchedEffect(Unit) {
        viewModel.onEvent(SettingsScreenEvent.OnLoadSettings)
    }

    val state by viewModel.state.collectAsStateWithLifecycle()
    val viewModelEvent by viewModel.event.collectAsStateWithLifecycle(initialValue = SharedViewModelEvents.None)
    val activityEvent by viewModel.activityFlow.collectAsStateWithLifecycle(initialValue = ActivityFlowEvents.None)
    LaunchedEffect(activityEvent) {
        when(activityEvent) {
            ActivityFlowEvents.None -> {}
            ActivityFlowEvents.RecreateActivity -> {
                recreateActivity()
            }
        }
    }
    SharedToastLogic(event = viewModelEvent)
    SettingsScreen(
        onReturn = onReturn,
        isDarkTheme = state.isDarkTheme,
        onEvent = viewModel::onEvent,
        numberOfCollections = state.numberOfCollections,
        imagesInCollection = state.imagesInCollection

    )
}

@Composable
private fun SettingsScreen(
    onReturn: () -> Unit,
    isDarkTheme: Boolean,
    numberOfCollections: Int,
    imagesInCollection: Int,
    onEvent: (SettingsScreenEvent) -> Unit
) {
    WallCraftAdditionalScreenWithScaffold(
        scaffoldTitle = stringResource(id = R.string.settings_screen_title),
        onReturn = onReturn,
    ) {
        SettingsItem(
            title = stringResource(id = R.string.setting_theme_title),
            description = stringResource(id = R.string.setting_theme_description),
            descriptionColor = MaterialTheme.colorScheme.error,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Light",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(modifier = Modifier.width(16.dp))
                Switch(
                    checked = isDarkTheme,
                    colors = colors(
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        checkedThumbColor = MaterialTheme.colorScheme.onBackground,
                        uncheckedTrackColor = MaterialTheme.colorScheme.onBackground,
                        uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                    ),
                    onCheckedChange = { isDark ->
                        onEvent(SettingsScreenEvent.OnUpdateTheme(isDark))
                    }
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Dark",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        SliderSettingsItem(
            title = stringResource(id = R.string.settings_n_collections_title),
            description = stringResource(id = R.string.settings_api_beware_desctiption),
            descriptionColor = MaterialTheme.colorScheme.onSecondary,
            sliderValue = numberOfCollections,
            onSliderChange = { newValue ->
                onEvent(SettingsScreenEvent.OnUpdateNCollections(newValue.toInt()))
            },
            onFinishChange = {
                onEvent(SettingsScreenEvent.OnNCollectionsChangeFinished)
            }
        )
        Spacer(modifier = Modifier.height(16.dp))
        SliderSettingsItem(
            title = stringResource(id = R.string.settings_n_images_in_collection_title),
            description = stringResource(id = R.string.settings_api_beware_desctiption) +
                          stringResource(id = R.string.settings_n_images_in_collection_description),
            descriptionColor = MaterialTheme.colorScheme.onSecondary,
            sliderValue = imagesInCollection,
            onSliderChange = { newValue ->
                onEvent(SettingsScreenEvent.OnUpdateNImagesInCollection(newValue.toInt()))
            },
            onFinishChange = {
                onEvent(SettingsScreenEvent.OnNImagesInCollectionChangeFinished)
            }
        )
    }
}

@Composable
fun SliderSettingsItem(
    title: String,
    description: String,
    descriptionColor: Color,
    sliderValue: Int,
    onSliderChange: (Float) -> Unit,
    onFinishChange: () -> Unit
) {
    SettingsItem(
        title = title,
        description = description,
        descriptionColor = descriptionColor,
    ) {
        Slider(
            value = sliderValue.toFloat(),
            onValueChange = onSliderChange,
            onValueChangeFinished = { onFinishChange() },
            valueRange = 1f..80f,
            steps = 80,
            colors = androidx.compose.material3.SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTickColor = MaterialTheme.colorScheme.onSecondary,
                inactiveTickColor = MaterialTheme.colorScheme.secondary
            )
        )
    }
}

@Composable
fun SettingsItem(
    title: String,
    description: String,
    descriptionColor: Color,
    modifier: Modifier = Modifier,
    content: @Composable (() -> Unit)
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = descriptionColor,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(4.dp))
        content()
    }
}


sealed interface SettingsScreenEvent {
    data object OnLoadSettings : SettingsScreenEvent
    data class OnUpdateTheme(val isDarkTheme: Boolean) : SettingsScreenEvent
    data class OnUpdateNCollections(val n: Int) : SettingsScreenEvent
    data object OnNCollectionsChangeFinished: SettingsScreenEvent
    data class OnUpdateNImagesInCollection(val n: Int) : SettingsScreenEvent
    data object OnNImagesInCollectionChangeFinished: SettingsScreenEvent

}
