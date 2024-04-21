package ru.dezerom.plugins.routings

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.dezerom.domain.use_case.AnsweringUseCase
import ru.dezerom.utils.getQueryParam
import ru.dezerom.utils.getToken
import ru.dezerom.utils.makeRespond
import ru.dezerom.utils.receiveOrNull

private const val CONTEXT_ID = "context_id"
private const val QUERY = "query"

private val answeringUseCase by lazy { AnsweringUseCase() }

fun Routing.answeringRouting() {

    get("/answer") {
        makeRespond {
            answeringUseCase.getAnswer(
                token = call.getToken(),
                contextId = call.getQueryParam(CONTEXT_ID),
                query = call.getQueryParam(QUERY)
            )
        }
    }

    post("/answer") {
        makeRespond {
            answeringUseCase.getAnswer(
                token = call.getToken(),
                questionDTO = call.receiveOrNull()
            )
        }
    }

}
