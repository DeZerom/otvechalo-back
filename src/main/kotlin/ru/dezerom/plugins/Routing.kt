package ru.dezerom.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.dezerom.plugins.routings.authRouting
import ru.dezerom.plugins.routings.contextRouting

fun Application.configureRouting() {
    routing {
        authRouting()
        contextRouting()
    }
}
