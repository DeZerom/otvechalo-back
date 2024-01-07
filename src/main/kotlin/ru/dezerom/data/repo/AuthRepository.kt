package ru.dezerom.data.repo

import ru.dezerom.domain.models.auth.CredentialsModel
import ru.dezerom.domain.models.call_result.CallResult
import ru.dezerom.domain.models.common.ActionResult
import ru.dezerom.domain.models.respond.RespondModel
import ru.dezerom.dto.common.BooleanDTO

class AuthRepository {

    suspend fun registerUser(credentials: CredentialsModel): CallResult<ActionResult> {
        return CallResult.Success(ActionResult(true))
    }

}
