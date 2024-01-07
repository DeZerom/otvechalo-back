package ru.dezerom.domain.models.respond

import io.ktor.http.*

enum class ErrorType {
    WRONG_DATA;

    val code: HttpStatusCode
        get() = when (this) {
            WRONG_DATA -> HttpStatusCode.BadRequest
        }

    val message: String = this.name
}
