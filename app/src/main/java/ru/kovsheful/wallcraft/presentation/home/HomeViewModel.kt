package ru.kovsheful.wallcraft.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.kovsheful.wallcraft.core.ConnectionTimedOut
import ru.kovsheful.wallcraft.core.SharedViewModelEvents
import ru.kovsheful.wallcraft.domain.use_cases.GetListOfCollections
import ru.kovsheful.wallcraft.domain.use_cases.GetTitleImageOfCollection
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getCollectionsList: GetListOfCollections,
    private val getTitleImageOfCollection: GetTitleImageOfCollection,
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _eventFlow: MutableSharedFlow<SharedViewModelEvents> = MutableSharedFlow()
    val event: SharedFlow<SharedViewModelEvents> = _eventFlow.asSharedFlow()

    companion object {
        const val TAG = "HomeViewModel"
    }

    fun onEvent(event: HomeScreenEvents) {
        viewModelScope.launch {
            when(event) {
                is HomeScreenEvents.OnLoadCollections -> {
                    // Separated getting the list of collections and their title images cause:
                    // 1. They don`t have one, so this requires separate request
                    // 2. Scalability. Maybe in future this app would meet requirement of getting
                    //       only titles of collections, and is such case there is ready use case
                    try {
                        val collections = getCollectionsList()
                        val updatedCollections = collections.map { collection ->
                            async {
                                collection.copy(imageUrl = getTitleImageOfCollection(collection.id))
                            }
                        }.awaitAll()
                        _state.value = _state.value.copy(collections = updatedCollections)
                    } catch(e: ConnectionTimedOut) {
                        Log.i(TAG, "OnLoadCollections exception: ${e.message}")
                        _eventFlow.emit(SharedViewModelEvents.OnShowToast("Server error, use VPN and try again"))
                    } catch(e: Exception) {
                        Log.i(TAG, "OnLoadCollections exception: ${e.message}")
                        _eventFlow.emit(SharedViewModelEvents.OnShowToast("Something went wrong, please, try again"))
                    }
                }

            }
        }
    }
}