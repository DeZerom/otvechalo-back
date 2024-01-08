package ru.dezerom.data.repo

import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import ru.dezerom.data.db.DatabaseSingleton
import ru.dezerom.data.db.tables.auth.CredentialsTable
import ru.dezerom.domain.models.auth.CredentialsModel
import ru.dezerom.domain.models.call_result.CallResult
import ru.dezerom.domain.models.common.ActionResult
import ru.dezerom.utils.makeAction

class AuthRepository {

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
        return DatabaseSingleton.dbQuery {
            CredentialsTable
                .select { CredentialsTable.login eq login }
                .firstOrNull() != null
        }
    }

}
