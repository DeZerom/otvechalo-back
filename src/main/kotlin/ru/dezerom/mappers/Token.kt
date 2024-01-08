package ru.dezerom.mappers

import org.jetbrains.exposed.sql.ResultRow
import ru.dezerom.data.db.tables.auth.TokensTable

fun ResultRow.rowToTokenString() = get(TokensTable.token)
