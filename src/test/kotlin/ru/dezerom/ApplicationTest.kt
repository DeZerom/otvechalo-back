package ru.dezerom

import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        assertEquals(4 ,2 + 2)
    }
}
