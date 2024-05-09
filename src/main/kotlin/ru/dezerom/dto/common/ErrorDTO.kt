package ru.dezerom.dto.common

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDTO(
    val error: String
)
