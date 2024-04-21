package ru.dezerom.data.models

import kotlinx.serialization.Serializable

@Serializable
data class AnswerDataModel(
    val answers: List<String> = emptyList(),
    val startSymbols: List<Int> = emptyList(),
    val probabilities: List<Double> = emptyList()
)
