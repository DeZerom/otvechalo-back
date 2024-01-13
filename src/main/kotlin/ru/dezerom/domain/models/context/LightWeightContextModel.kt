package ru.dezerom.domain.models.context

import java.util.*

data class LightWeightContextModel(
    val id: UUID,
    val name: String,
    val description: String,
    val hash: String
)
