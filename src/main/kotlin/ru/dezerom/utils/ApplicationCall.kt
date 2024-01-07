package ru.dezerom.utils

import io.ktor.server.application.*
import io.ktor.server.request.*

suspend inline fun <reified T>ApplicationCall.receiveOrNull(): T? {
    return runCatching { receiveNullable<T>() }.getOrNull()
}
