package ru.dezerom.mappers

import org.jetbrains.exposed.sql.ResultRow
import ru.dezerom.data.db.tables.context.ContextTable
import ru.dezerom.domain.models.context.LightWeightContextModel
import ru.dezerom.domain.models.context.RichContextModel
import ru.dezerom.dto.context.ContextLightWeightDTO
import ru.dezerom.dto.context.RichContextDTO

fun ResultRow.toRichContext() = RichContextModel(
    id = get(ContextTable.id).value,
    name = get(ContextTable.name),
    description = get(ContextTable.description),
    contextHash = get(ContextTable.contextHash),
    context = get(ContextTable.context),
    authorId = get(ContextTable.authorId).value
)

fun RichContextModel.toDTO() = RichContextDTO(
    id = id.toString(),
    name = name,
    description = description,
    hash = contextHash,
    context = context
)

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
