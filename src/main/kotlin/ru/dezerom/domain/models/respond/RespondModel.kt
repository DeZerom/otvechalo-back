package ru.dezerom.domain.models.respond

sealed class RespondModel<out T> {

    data class SuccessRespondModel<T>(
        val body: T
    ) : RespondModel<T>()

    data class ErrorRespondModel(
        val error: ErrorType
    ) : RespondModel<Nothing>()

}
