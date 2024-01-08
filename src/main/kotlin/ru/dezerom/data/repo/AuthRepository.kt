package ru.dezerom.data.repo

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import ru.dezerom.data.db.DatabaseSingleton
import ru.dezerom.data.db.tables.auth.CredentialsTable
import ru.dezerom.data.db.tables.auth.TokensTable
import ru.dezerom.domain.models.auth.CredentialsModel
import ru.dezerom.domain.models.call_result.CallResult
import ru.dezerom.domain.models.common.ActionResult
import ru.dezerom.domain.models.respond.ErrorType
import ru.dezerom.mappers.toDomain
import ru.dezerom.utils.handle
import ru.dezerom.utils.makeAction
import ru.dezerom.utils.makeCall
import java.util.*

class AuthRepository {

    suspend fun getUserByToken(token: String): CallResult<CredentialsModel> {
        return makeCall(
            call = {
                DatabaseSingleton.dbQuery {
                    CredentialsTable.innerJoin(TokensTable)
                        .select { (CredentialsTable.id eq TokensTable.userId) and (TokensTable.token eq token) }
                        .firstOrNull()
                        ?.toDomain()
                }
            },
            onNull = { CallResult.Error(ErrorType.noAccess()) }
        )
    }

    suspend fun createToken(userId: UUID): CallResult<String> {
        val token = UUID.randomUUID().toString()

        return makeAction(
            action = {
                DatabaseSingleton.dbQuery {
                    TokensTable.insert {
                        it[this.userId] = EntityID(userId, CredentialsTable)
                        it[this.token] = token
                    }.insertedCount > 0
                }
            }
        ).handle(
            onSuccess = { CallResult.Success(token) },
            onError = { CallResult.Error(it.errorType) }
        )
    }

    suspend fun registerUser(credentials: CredentialsModel): CallResult<ActionResult> {
        return makeAction(
            action = {
                DatabaseSingleton.dbQuery {
                    CredentialsTable.insert {
                        it[login] = credentials.login
                        it[password] = credentials.password
                        it[salt] = credentials.salt
                    }.insertedCount > 0
                }
            }
        )
    }

    suspend fun isUserExists(login: String): Boolean {
        return getUser(login).handle(
            onSuccess = { true },
            onError = { false }
        )
    }

    suspend fun getUser(login: String): CallResult<CredentialsModel> {
        return makeCall(
            call = {
                DatabaseSingleton.dbQuery {
                    CredentialsTable
                        .select { CredentialsTable.login eq login }
                        .firstOrNull()
                        ?.toDomain()
                }
            },
            onNull = { CallResult.Error(ErrorType.NotFound(ErrorType.Reasons.USER_NOT_EXISTS)) }
        )
    }

}
