package ru.dezerom

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import ru.dezerom.data.db.DatabaseSingleton
import ru.dezerom.plugins.configureRouting
import ru.dezerom.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    DatabaseSingleton.init()
    configureSerialization()
    configureRouting()
}
