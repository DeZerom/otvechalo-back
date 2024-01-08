package ru.dezerom.data.db.tables.auth

import org.jetbrains.exposed.dao.id.UUIDTable

object CredentialsTable: UUIDTable() {
    val login = varchar(name = "login", length = 32)
    val password = varchar(name = "password", length = 64)
    val salt = varchar(name = "salt", length = 36)
}
