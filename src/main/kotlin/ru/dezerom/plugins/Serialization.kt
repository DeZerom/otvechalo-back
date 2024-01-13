package ru.dezerom.plugins

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.doublereceive.*

fun Application.configureSerialization() {
    install(DoubleReceive)

    install(ContentNegotiation) {
        json()
    }
}
