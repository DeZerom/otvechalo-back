package ru.dezerom.data.db.tables.auth

import org.jetbrains.exposed.sql.Table

object Credentials: Table() {
    val id = uuid("id")
    val login = varchar(name = "login", length = 32)
    val password = varchar(name = "password", length = 64)
    val salt = varchar(name = "salt", length = 36)
}
