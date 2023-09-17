package ru.kovsheful.wallcraft.utils

import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import retrofit2.HttpException
import ru.kovsheful.wallcraft.core.ConnectionTimedOut
import ru.kovsheful.wallcraft.core.SharedViewModelEvents
import ru.kovsheful.wallcraft.core.TooManyRequests
import ru.kovsheful.wallcraft.core.UnknownHttpError
import ru.kovsheful.wallcraft.presentation.home.HomeViewModel

suspend fun <T> apiCall(call: suspend () -> T): T {
    return try {
        call()
    } catch (e: HttpException) {
        when (e.code()) {
            522 -> throw ConnectionTimedOut("522")
            429 -> throw TooManyRequests("429")
            else -> throw UnknownHttpError(e.code().toString())
        }
    } catch (e: Exception) {
        throw e
    }
}

suspend fun catchSharedViewModelException(
    //Better in future provide to function callback to emit smth, not flow itself
    flow: MutableSharedFlow<SharedViewModelEvents>,
    exception: Exception,
    tagForLog: String,
    eventName: String
) {
    when (exception) {
        is ConnectionTimedOut -> {
            Log.i(tagForLog, "$eventName ConnectionTimedOut exception: ${exception.message}")
            flow.emit(SharedViewModelEvents.OnShowToast("Server error, use VPN and try again"))
        }
        is UnknownHttpError -> {
            Log.i(tagForLog, "$eventName UnknownHttpError exception: ${exception.message}")
            flow.emit(SharedViewModelEvents.OnShowToast("Internet error, use VPN and try again"))
        }
        is TooManyRequests -> {
            Log.i(tagForLog, "$eventName TooManyRequests exception: ${exception.message}")
            flow.emit(SharedViewModelEvents.OnShowToast("Too many requests, try out in an hour later"))
        }
        else -> {
            Log.i(tagForLog, "$eventName exception: ${exception.message}")
            flow.emit(SharedViewModelEvents.OnShowToast("Something went wrong, please, try again"))
        }
    }
}