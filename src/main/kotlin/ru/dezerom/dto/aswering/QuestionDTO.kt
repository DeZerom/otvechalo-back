package ru.dezerom.dto.aswering

import kotlinx.serialization.Serializable

@Serializable
data class QuestionDTO(
    val context: String? = null,
    val query: String? = null
)
