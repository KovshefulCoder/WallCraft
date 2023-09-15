package ru.kovsheful.wallcraft.utils

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import ru.kovsheful.wallcraft.core.ConnectionTimedOut
import ru.kovsheful.wallcraft.core.SharedViewModelEvents
import ru.kovsheful.wallcraft.core.UnknownHttpError
import ru.kovsheful.wallcraft.presentation.home.HomeViewModel

suspend fun <T> apiCall(call: suspend () -> T): T {
    return try {
        call()
    } catch (e: HttpException) {
        when (e.code()) {
            522 -> throw ConnectionTimedOut("522")
            else -> throw UnknownHttpError(e.code().toString())
        }
    } catch (e: Exception) {
        throw e
    }
}

suspend fun catchSharedViewModelException(
    //Better in future provide to function callback to emit smth, not flow itself
    flow: MutableSharedFlow<SharedViewModelEvents>,
    exception: Exception
) {
    when (exception) {
        is ConnectionTimedOut -> {
            Log.i(HomeViewModel.TAG, "OnLoadCollections ConnectionTimedOut exception: ${exception.message}")
            flow.emit(SharedViewModelEvents.OnShowToast("Server error, use VPN and try again"))
        }
        is UnknownHttpError -> {
            Log.i(HomeViewModel.TAG, "OnLoadCollections UnknownHttpError exception: ${exception.message}")
            flow.emit(SharedViewModelEvents.OnShowToast("Interten error, use VPN and try again"))
        }
        else -> {
            Log.i(HomeViewModel.TAG, "OnLoadCollections exception: ${exception.message}")
            flow.emit(SharedViewModelEvents.OnShowToast("Something went wrong, please, try again"))
        }
    }
}