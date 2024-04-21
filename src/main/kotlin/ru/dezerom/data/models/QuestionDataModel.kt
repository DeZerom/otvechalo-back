package ru.dezerom.data.models

import kotlinx.serialization.Serializable

@Serializable
data class QuestionDataModel(
    val query: String,
    val context: String
)
