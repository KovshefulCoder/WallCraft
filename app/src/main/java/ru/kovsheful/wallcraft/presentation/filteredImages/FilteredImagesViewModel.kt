package ru.kovsheful.wallcraft.presentation.filteredImages

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
import ru.kovsheful.wallcraft.domain.use_cases.images.GetDownloadedImages
import ru.kovsheful.wallcraft.domain.use_cases.images.GetFavoriteImages
import javax.inject.Inject


@HiltViewModel
class FilteredImagesViewModel @Inject constructor(
    private val getFavoriteImages: GetFavoriteImages,
    private val getDownloadedImages: GetDownloadedImages
) : ViewModel() {
    private val _state = MutableStateFlow(FilteredImagesState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<SharedViewModelEvents> = MutableSharedFlow()
    val event: SharedFlow<SharedViewModelEvents> = _eventFlow.asSharedFlow()

    companion object {
        const val TAG = "FullScreenImageViewModel"
    }

    fun onEvent(event: FilteredImagesScreenEvent) {
        viewModelScope.launch {
            when (event) {
                is FilteredImagesScreenEvent.OnLoadImagesByFilter -> {
                    _state.update { curValue ->
                        curValue.copy(
                            imagesLinks = when(event.filter) {
                                DOWNLOADED -> getDownloadedImages()
                                FAVORITE -> getFavoriteImages()
                                else -> {
                                    _eventFlow.emit(SharedViewModelEvents.OnShowToast("No such filter"))
                                    listOf()
                                }
                            }
                        )
                    }
                }
            }
        }
    }

}