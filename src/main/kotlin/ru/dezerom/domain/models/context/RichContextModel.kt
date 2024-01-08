package ru.dezerom.domain.models.context

import java.util.*

data class RichContextModel(
    val id: UUID,
    val authorId: UUID,
    val name: String,
    val description: String,
    val context: String,
    val contextHash: String
)
