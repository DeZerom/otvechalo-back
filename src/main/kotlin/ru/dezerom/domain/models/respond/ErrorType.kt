package ru.dezerom.domain.models.respond

import io.ktor.http.*

sealed class ErrorType {

    val code: HttpStatusCode
        get() = when (this) {
            is WrongData -> HttpStatusCode.BadRequest
            is InternalError -> HttpStatusCode.InternalServerError
        }

    open val reason: String = Reasons.EMPTY

    data class WrongData(override val reason: String): ErrorType()

    data class InternalError(override val reason: String): ErrorType()

    object Reasons {
        const val EMPTY = ""

        const val USER_EXISTS = "user exists"

        const val EMPTY_VALUES = "one of the values is empty"
    }

    companion object {
        fun internal(reason: String = Reasons.EMPTY) = InternalError(reason)
    }
}
