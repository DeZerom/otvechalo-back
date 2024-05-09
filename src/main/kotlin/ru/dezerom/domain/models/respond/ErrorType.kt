package ru.dezerom.domain.models.respond

import io.ktor.http.*

sealed class ErrorType {

    val code: HttpStatusCode
        get() = when (this) {
            is WrongData -> HttpStatusCode.BadRequest
            is Forbidden -> HttpStatusCode.Forbidden
            is NotFound -> HttpStatusCode.NotFound
            is InternalError -> HttpStatusCode.InternalServerError
        }

    open val reason: String = Reasons.EMPTY

    data class WrongData(override val reason: String): ErrorType()

    data class Forbidden(override val reason: String): ErrorType()

    data class NotFound(override val reason: String): ErrorType()

    data class InternalError(override val reason: String): ErrorType()

    object Reasons {
        const val EMPTY = ""

        const val USER_EXISTS = "Пользователь уже существует"
        const val USER_NOT_EXISTS = "Неверный токен авторизации"
        const val WRONG_PASS = "Неверный пароль"

        const val EMPTY_VALUES = "Отправлены не все данные"
        const val NOTHING_FOUND = "Ничего не найледено"
    }

    companion object {
        fun internal(reason: String = Reasons.EMPTY) = InternalError(reason)

        fun emptyValues() = WrongData(Reasons.EMPTY_VALUES)

        fun noAccess() = Forbidden(Reasons.USER_NOT_EXISTS)

        fun nothingFound() = NotFound(Reasons.NOTHING_FOUND)
    }
}
