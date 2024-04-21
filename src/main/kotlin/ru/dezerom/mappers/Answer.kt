package ru.dezerom.mappers

import kotlinx.serialization.json.*
import ru.dezerom.data.models.AnswerDataModel

private const val ANSWER_KEY = "answer"

fun JsonObject.toAnswerDataModel(): AnswerDataModel {
    val arrays = get(ANSWER_KEY)?.jsonArray

    val answers = arrays?.firstOrNull()?.jsonArray?.mapNotNull { it.jsonPrimitive.toString() }
    val startSymbols = arrays?.firstOrNull()?.jsonArray?.mapNotNull { it.jsonPrimitive.intOrNull }
    val prob = arrays?.firstOrNull()?.jsonArray?.mapNotNull { it.jsonPrimitive.doubleOrNull }

    return AnswerDataModel(
        answers = answers ?: emptyList(),
        startSymbols = startSymbols ?: emptyList(),
        probabilities = prob ?: emptyList()
    )
}
