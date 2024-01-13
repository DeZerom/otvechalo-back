package ru.dezerom.dto.context

import kotlinx.serialization.Serializable

@Serializable
data class ContextLightWeightDTO(
    val id: String,
    val name: String,
    val description: String,
    val hash: String,
)
