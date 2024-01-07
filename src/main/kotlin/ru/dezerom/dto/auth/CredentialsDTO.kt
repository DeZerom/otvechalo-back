package ru.dezerom.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class CredentialsDTO(
    val login: String? = null,
    val password: String? = null
)
