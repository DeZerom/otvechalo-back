package ru.dezerom.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.util.pipeline.*
import ru.dezerom.domain.models.respond.RespondModel
import ru.dezerom.dto.common.ErrorDTO

suspend inline fun <reified T : Any>PipelineContext<Unit, ApplicationCall>.makeRespond(block: () -> RespondModel<T>) {
    when (val response = block()) {
        is RespondModel.SuccessRespondModel ->
            call.respond(HttpStatusCode.OK, response.body)

        is RespondModel.ErrorRespondModel ->
            call.respond(response.error.code, ErrorDTO(response.error.reason))
    }
}
