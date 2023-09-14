package ru.kovsheful.wallcraft.utils

import retrofit2.HttpException
import ru.kovsheful.wallcraft.core.ConnectionTimedOut
import ru.kovsheful.wallcraft.core.UnknownHttpError

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