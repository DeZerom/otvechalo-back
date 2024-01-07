package ru.dezerom.mappers

import ru.dezerom.domain.models.common.ActionResult
import ru.dezerom.dto.common.BooleanDTO

fun ActionResult.toDTO() = BooleanDTO(result)
