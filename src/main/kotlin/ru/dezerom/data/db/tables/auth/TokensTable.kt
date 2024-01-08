package ru.dezerom.data.db.tables.auth

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption

object TokensTable: UUIDTable() {
    val userId = reference(
        name = "user_id",
        refColumn = CredentialsTable.id,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )

    val token = varchar(name = "token", length = 36)
}
