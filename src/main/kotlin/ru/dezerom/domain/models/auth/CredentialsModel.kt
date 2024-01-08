package ru.dezerom.domain.models.auth

import java.util.*

data class CredentialsModel(
    val id: UUID,
    val login: String,
    val password: String,
    val salt: String
)
