package ru.dezerom.dto.context

import kotlinx.serialization.Serializable

@Serializable
data class RichContextDTO(
    val id: String,
    val name: String,
    val description: String,
    val hash: String,
    val context: String
)
