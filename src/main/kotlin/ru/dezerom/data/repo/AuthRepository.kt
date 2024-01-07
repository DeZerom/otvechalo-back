package ru.dezerom.data.repo

import ru.dezerom.domain.models.auth.CredentialsModel
import ru.dezerom.domain.models.call_result.CallResult
import ru.dezerom.domain.models.common.ActionResult

class AuthRepository {

    suspend fun registerUser(credentials: CredentialsModel): CallResult<ActionResult> {
        return CallResult.Success(ActionResult(true))
    }

}
