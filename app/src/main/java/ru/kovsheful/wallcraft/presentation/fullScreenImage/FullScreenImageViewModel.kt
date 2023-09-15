package ru.kovsheful.wallcraft.presentation.fullScreenImage

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
import ru.kovsheful.wallcraft.domain.use_cases.GetHighQualityImage
import ru.kovsheful.wallcraft.domain.use_cases.GetImagesOfCollection
import ru.kovsheful.wallcraft.presentation.collectionImages.CollectionImagesScreenEvents
import ru.kovsheful.wallcraft.presentation.collectionImages.CollectionImagesState
import javax.inject.Inject


@HiltViewModel
class FullScreenImageViewModel @Inject constructor(
    private val getHighQualityImage: GetHighQualityImage
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
                    _state.update { curValue ->
                        curValue.copy(
                            highQualityImgageUrl = getHighQualityImage(event.imageID)
                        )
                    }
                }
            }
        }
    }
}