package ru.kovsheful.wallcraft.presentation.fullScreenImage

import android.util.Log
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
import ru.kovsheful.wallcraft.core.ImageAlreadyHaveThisStatus
import ru.kovsheful.wallcraft.core.SharedViewModelEvents
import ru.kovsheful.wallcraft.domain.models.ImageModel
import ru.kovsheful.wallcraft.domain.use_cases.DownloadImageByUrl
import ru.kovsheful.wallcraft.domain.use_cases.GetHighQualityImage
import ru.kovsheful.wallcraft.domain.use_cases.SetImageAsWallpaper
import javax.inject.Inject


@HiltViewModel
class FullScreenImageViewModel @Inject constructor(
    private val getHighQualityImage: GetHighQualityImage,
    private val setImageAsWallpaper: SetImageAsWallpaper,
    private val downloadImageByUrl: DownloadImageByUrl
) : ViewModel() {
    private val _state = MutableStateFlow(FullScreenImageState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<SharedViewModelEvents> = MutableSharedFlow()
    val event: SharedFlow<SharedViewModelEvents> = _eventFlow.asSharedFlow()

    companion object {
        const val TAG = "FullScreenImageViewModel"
    }

    fun onEvent(event: FullScreenImageEvent) {
        viewModelScope.launch {
            when(event) {
                is FullScreenImageEvent.OnLoadImageInHighQuality -> {
                    val image = getHighQualityImage(event.imageID)
                    _state.update { curValue ->
                        curValue.copy(
                            image = image
                        )
                    }
                }
                is FullScreenImageEvent.OnSetAsWallpaper -> {
                    sharedVMLogic {
                        setImageAsWallpaper(state.value.image, event.wallpaperType)
                    }
                }
                is FullScreenImageEvent.OnDownloadImage -> {
                    sharedVMLogic {
                        downloadImageByUrl(state.value.image)
                    }
                }
                else -> {}
            }
        }
    }

    private suspend fun sharedVMLogic(
        useCase: suspend () -> Unit
    ) {
        try {
            _state.update { curValue ->
                curValue.copy(onLoading = true)
            }
            useCase()
            _eventFlow.emit(SharedViewModelEvents.OnShowToast("Success"))
        } catch (e: ImageAlreadyHaveThisStatus) {
            _eventFlow.emit(SharedViewModelEvents.OnShowToast(e.message))

        } catch (e: Exception) {
            Log.i(TAG, "Ex message: " + (e.message ?: "No message"))
            _eventFlow.emit(SharedViewModelEvents.OnShowToast(e.message ?: "Unknown error"))
        } finally {
            _state.update { curValue -> curValue.copy( onLoading = false) }
        }
    }
}
