package ru.dezerom.plugins.routings

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.dezerom.domain.use_case.AuthUseCase
import ru.dezerom.utils.makeRespond
import ru.dezerom.utils.receiveOrNull

val authUseCase by lazy { AuthUseCase() }

fun Routing.authRouting() {

    post("/register") { makeRespond { authUseCase.register(call.receiveOrNull()) } }

    post("/auth") { makeRespond { authUseCase.authorize(call.receiveOrNull()) } }

}
