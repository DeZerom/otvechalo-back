package ru.dezerom.data.repo

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.JsonObject
import ru.dezerom.data.models.QuestionDataModel
import ru.dezerom.domain.models.call_result.CallResult
import ru.dezerom.domain.models.respond.ErrorType
import ru.dezerom.mappers.toAnswerDataModel
import ru.dezerom.utils.makeCall

class AnsweringRepository {

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) { json() }
    }

    suspend fun getAnswer(context: String, query: String): CallResult<String> {
        return makeCall(
            onNull = { CallResult.Error(ErrorType.internal()) },
            call = {
                val body = client.post {
                    url("http://127.0.0.1:5001/answer-question")
                    contentType(ContentType.Application.Json)
                    setBody(QuestionDataModel(query, context))
                }.body<JsonObject>()

                body.toAnswerDataModel().answers.firstOrNull()
            }
        )
    }

}
