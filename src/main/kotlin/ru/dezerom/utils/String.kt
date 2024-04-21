package ru.dezerom.utils

import java.security.MessageDigest
import java.util.*

fun String?.toUUID(): UUID? {
    return try {
        UUID.fromString(this)
    } catch (_: IllegalArgumentException) {
        null
    }
}

fun String.sha256Hash(): String {
    val bytes = this.toByteArray()

    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(bytes)

    return digest.fold("") { str, it -> str + "%02x".format(it) }
}
