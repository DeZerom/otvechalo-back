package ru.dezerom.dto.context

import kotlinx.serialization.Serializable

@Serializable
data class SaveContextDTO(
    val context: String? = null,
    val name: String? = null,
    val description: String? = null
)
