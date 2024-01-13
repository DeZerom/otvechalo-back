package ru.dezerom.data.repo

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import ru.dezerom.data.db.DatabaseSingleton
import ru.dezerom.data.db.tables.auth.CredentialsTable
import ru.dezerom.data.db.tables.context.ContextTable
import ru.dezerom.domain.models.call_result.CallResult
import ru.dezerom.domain.models.common.ActionResult
import ru.dezerom.domain.models.context.LightWeightContextModel
import ru.dezerom.domain.models.context.RichContextModel
import ru.dezerom.domain.models.respond.ErrorType
import ru.dezerom.mappers.toLightWeightContext
import ru.dezerom.mappers.toRichContext
import ru.dezerom.utils.makeAction
import ru.dezerom.utils.makeCall
import java.util.*

class ContextRepository {

    suspend fun getContextDetails(id: UUID): CallResult<RichContextModel> {
        return makeCall(
            onNull = { CallResult.Error(ErrorType.nothingFound()) },
            call = {
                DatabaseSingleton.dbQuery {
                    ContextTable
                        .select { ContextTable.id eq id }
                        .firstOrNull()
                        ?.toRichContext()
                }
            }
        )
    }

    suspend fun getContextsLightWeight(authorId: UUID): CallResult<List<LightWeightContextModel>> {
        return makeCall(
            onNull = { CallResult.Error(ErrorType.internal()) },
            call = {
                DatabaseSingleton.dbQuery {
                    ContextTable
                        .slice(ContextTable.id, ContextTable.name, ContextTable.description, ContextTable.contextHash)
                        .select { ContextTable.authorId eq authorId }
                        .map(ResultRow::toLightWeightContext)
                }
            }
        )
    }

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
