package ru.kovsheful.wallcraft.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.kovsheful.wallcraft.core.SharedViewModelEvents
import ru.kovsheful.wallcraft.domain.repository.SettingsType
import ru.kovsheful.wallcraft.domain.use_cases.settings.GetAppSettings
import ru.kovsheful.wallcraft.domain.use_cases.settings.UpdateAppSettings
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getAppSettings: GetAppSettings,
    private val updateAppSettings: UpdateAppSettings,
) : ViewModel() {
    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<SharedViewModelEvents> = MutableSharedFlow()
    val event: SharedFlow<SharedViewModelEvents> = _eventFlow.asSharedFlow()

    private val _activityFlow: MutableSharedFlow<ActivityFlowEvents> = MutableSharedFlow()
    val activityFlow = _activityFlow.asSharedFlow()

    companion object {
        const val TAG = "SettingsViewModel"
    }

    fun onEvent(event: SettingsScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsScreenEvent.OnLoadSettings -> {
                    val settings = getAppSettings()
                    _state.update { curValue ->
                        curValue.copy(
                            isDarkTheme = settings.isDarkTheme,
                            numberOfCollections = settings.numberOfCollections,
                            imagesInCollection = settings.imagesInCollection
                        )
                    }
                }
                is SettingsScreenEvent.OnUpdateTheme -> {
                    _state.update { curValue ->
                        curValue.copy(
                            isDarkTheme = event.isDarkTheme
                        )
                    }
                    updateAppSettings(SettingsType.Theme(state.value.isDarkTheme))
                    _activityFlow.emit(ActivityFlowEvents.RecreateActivity)
                }
                is SettingsScreenEvent.OnUpdateNCollections -> {
                    _state.update { curValue ->
                        curValue.copy(
                            numberOfCollections = event.n
                        )
                    }
                }
                is SettingsScreenEvent.OnUpdateNImagesInCollection -> {
                    _state.update { curValue ->
                        curValue.copy(
                            imagesInCollection = event.n
                        )
                    }
                }
                is SettingsScreenEvent.OnNCollectionsChangeFinished -> {
                    updateAppSettings(
                        SettingsType.NumberOfCollections(state.value.numberOfCollections)
                    )
                }
                is SettingsScreenEvent.OnNImagesInCollectionChangeFinished -> {
                    updateAppSettings(
                        SettingsType.ImagesInCollection(state.value.imagesInCollection)
                    )
                }
            }
        }
    }
}

sealed interface ActivityFlowEvents {
    data object None: ActivityFlowEvents
    data object RecreateActivity: ActivityFlowEvents
}