package ru.kovsheful.wallcraft.presentation.collectionImages

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
import ru.kovsheful.wallcraft.domain.use_cases.GetImagesOfCollection
import javax.inject.Inject


@HiltViewModel
class CollectionImagesViewModel @Inject constructor(
    private val getImagesOfCollection: GetImagesOfCollection
) : ViewModel() {
    private val _state = MutableStateFlow(CollectionImagesState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<SharedViewModelEvents> = MutableSharedFlow()
    val event: SharedFlow<SharedViewModelEvents> = _eventFlow.asSharedFlow()

    companion object {
        const val TAG = "CollectionImagesViewModel"
    }

    fun onEvent(event: CollectionImagesScreenEvents) {
        viewModelScope.launch {
            when(event) {
                is CollectionImagesScreenEvents.OnLoadImages -> {
                    _state.update { curUpdate ->
                        curUpdate.copy(
                            images = getImagesOfCollection(event.id)
                        )
                    }
                }
            }
        }
    }
}

