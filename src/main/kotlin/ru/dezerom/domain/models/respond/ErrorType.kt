package ru.dezerom.domain.models.respond

import io.ktor.http.*

sealed class ErrorType {

    val code: HttpStatusCode
        get() = when (this) {
            is WrongData -> HttpStatusCode.BadRequest
            is NotFound -> HttpStatusCode.NotFound
            is InternalError -> HttpStatusCode.InternalServerError
        }

    open val reason: String = Reasons.EMPTY

    data class WrongData(override val reason: String): ErrorType()

    data class NotFound(override val reason: String): ErrorType()

    data class InternalError(override val reason: String): ErrorType()

    object Reasons {
        const val EMPTY = ""

        const val USER_EXISTS = "user_already_exists"
        const val USER_NOT_EXISTS = "user_not_exists"
        const val WRONG_PASS = "wrong_password"

        const val EMPTY_VALUES = "one_of_the_values_is_empty"
    }

    companion object {
        fun internal(reason: String = Reasons.EMPTY) = InternalError(reason)

        fun emptyValues() = WrongData(Reasons.EMPTY_VALUES)
    }
}
