package ru.dezerom.data.db

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import ru.dezerom.data.db.tables.auth.Credentials

object DatabaseSingleton {

    private var database: Database? = null

    fun init() {
        initConnection()

        transaction(database) { createTables() }
    }

    private fun initConnection() {
        Database.connect(
            url = DbSecrets.DB_URL,
            driver = "org.postgresql.Driver"
        )
    }

    private fun createTables() {
        SchemaUtils.create(Credentials)
    }

}
