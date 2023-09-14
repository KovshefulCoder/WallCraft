package ru.kovsheful.wallcraft.presentation.collectionImages

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.kovsheful.wallcraft.presentation.home.SharedViewModelEvents
import javax.inject.Inject


@HiltViewModel
class CollectionImagesViewModel @Inject constructor(

) : ViewModel() {
    private val _state = MutableStateFlow(CollectionImagesState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<SharedViewModelEvents> = MutableSharedFlow()
    val event: SharedFlow<SharedViewModelEvents> = _eventFlow.asSharedFlow()

    companion object {
        const val TAG = "HomeViewModel"
    }
}