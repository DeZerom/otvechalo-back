package ru.dezerom.data.db

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import ru.dezerom.data.db.tables.auth.CredentialsTable
import ru.dezerom.data.db.tables.auth.TokensTable
import ru.dezerom.data.db.tables.context.ContextTable

object DatabaseSingleton {

    private var database: Database? = null

    fun init() {
        initConnection()

        transaction(database) { createTables() }
    }

    suspend fun <T>dbQuery(query: () -> T) = newSuspendedTransaction(Dispatchers.IO, database) { query() }

    private fun initConnection() {
        Database.connect(
            url = DbSecrets.DB_URL,
            user = DbSecrets.DB_USER,
            password = DbSecrets.DB_PASSWORD,
            driver = "org.postgresql.Driver"
        )
    }

    private fun createTables() {
        SchemaUtils.create(CredentialsTable, TokensTable, ContextTable)
    }

}
