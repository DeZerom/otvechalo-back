package ru.dezerom.domain.models.call_result

import ru.dezerom.domain.models.respond.ErrorType

sealed class CallResult<T> {

    data class Success<T>(
        val body: T
    ): CallResult<T>()

    data class Error(
        val errorType: ErrorType
    ): CallResult<Nothing>()

}
