package ru.dezerom.mappers

import org.jetbrains.exposed.sql.ResultRow
import ru.dezerom.data.db.tables.auth.CredentialsTable
import ru.dezerom.domain.models.auth.CredentialsModel

fun ResultRow.rowToCredentials() = CredentialsModel(
    id = get(CredentialsTable.id).value,
    login = get(CredentialsTable.login),
    password = get(CredentialsTable.password),
    salt = get(CredentialsTable.salt)
)
