package ru.kovsheful.wallcraft.presentation.home

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor() : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

//    private val _eventFlow: MutableSharedFlow<HomeViewModelEvents> = MutableSharedFlow(replay = 1)
//    val event: SharedFlow<HomeViewModelEvents> = _eventFlow.asSharedFlow()
}