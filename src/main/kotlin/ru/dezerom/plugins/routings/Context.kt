package ru.dezerom.plugins.routings

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.dezerom.domain.use_case.ContextUseCase
import ru.dezerom.utils.getQueryParam
import ru.dezerom.utils.getToken
import ru.dezerom.utils.makeRespond
import ru.dezerom.utils.receiveOrNull

private val contextUseCase by lazy { ContextUseCase() }

private const val CONTEXT_ID = "context_id"

fun Routing.contextRouting() {

    post("/save_context") { makeRespond { contextUseCase.saveContext(call.getToken(), call.receiveOrNull()) } }

    get("/all_contexts_light_weight") { makeRespond { contextUseCase.getLightWeightContexts(call.getToken()) } }

    get("context_rich") {
        makeRespond { contextUseCase.getContextDetails(call.getToken(), call.getQueryParam(CONTEXT_ID)) }
    }

}
