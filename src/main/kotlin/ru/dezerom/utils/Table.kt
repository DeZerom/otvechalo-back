package ru.dezerom.utils

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.Table

fun <T: Comparable<T>> Table.defaultReference(name: String, refColumn: Column<EntityID<T>>) = reference(
    name = name,
    refColumn = refColumn,
    onDelete = ReferenceOption.CASCADE,
    onUpdate = ReferenceOption.CASCADE
)
