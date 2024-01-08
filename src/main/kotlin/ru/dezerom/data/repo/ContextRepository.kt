package ru.dezerom.data.repo

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.insert
import ru.dezerom.data.db.DatabaseSingleton
import ru.dezerom.data.db.tables.auth.CredentialsTable
import ru.dezerom.data.db.tables.context.ContextTable
import ru.dezerom.domain.models.call_result.CallResult
import ru.dezerom.domain.models.common.ActionResult
import ru.dezerom.domain.models.context.RichContextModel
import ru.dezerom.utils.makeAction

class ContextRepository {

    suspend fun saveContext(context: RichContextModel): CallResult<ActionResult> {
        return makeAction(
            action = {
                DatabaseSingleton.dbQuery {
                    ContextTable.insert {
                        it[id] = context.id
                        it[name] = context.name
                        it[description] = context.description
                        it[authorId] = EntityID(context.authorId, CredentialsTable)
                        it[this.context] = context.context
                        it[contextHash] = context.contextHash
                    }.insertedCount > 0
                }
            }
        )
    }

}
