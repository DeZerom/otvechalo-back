package ru.dezerom.utils

import io.ktor.server.application.*
import io.ktor.server.request.*

private const val TOKEN_HEADER = "AuthTokenHeader"

fun ApplicationCall.getToken(): String? {
    return request.header(TOKEN_HEADER)
}

suspend inline fun <reified T> ApplicationCall.receiveOrNull(): T? {
    return runCatching { receiveNullable<T>() }.getOrNull()
}
