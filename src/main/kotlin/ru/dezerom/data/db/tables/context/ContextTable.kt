package ru.dezerom.data.db.tables.context

import org.jetbrains.exposed.dao.id.IdTable
import ru.dezerom.data.db.tables.auth.CredentialsTable
import ru.dezerom.utils.defaultReference
import java.util.*

object ContextTable: IdTable<UUID>() {
    override val id = uuid("id").entityId()

    val authorId = defaultReference(name = "authorId", CredentialsTable.id)

    val name = varchar(name = "context_name", length = 50)
    val description = varchar(name = "description", length = 256)
    val context = text(name = "context")
    val contextHash = varchar(name = "hash", length = 64)
}
