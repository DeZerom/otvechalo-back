package ru.dezerom.mappers

import org.jetbrains.exposed.sql.ResultRow
import ru.dezerom.data.db.tables.context.ContextTable
import ru.dezerom.domain.models.context.LightWeightContextModel
import ru.dezerom.dto.context.ContextLightWeightDTO

fun ResultRow.toLightWeightContext() = LightWeightContextModel(
    id = get(ContextTable.id).value,
    name = get(ContextTable.name),
    description = get(ContextTable.description),
    hash = get(ContextTable.contextHash)
)

fun LightWeightContextModel.toDTO() = ContextLightWeightDTO(
    id = id.toString(),
    name = name,
    description = description,
    hash = hash
)
