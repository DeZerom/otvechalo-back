package ru.dezerom.domain.use_case

import ru.dezerom.data.repo.AuthRepository
import ru.dezerom.data.repo.ContextRepository
import ru.dezerom.domain.models.auth.CredentialsModel
import ru.dezerom.domain.models.context.RichContextModel
import ru.dezerom.domain.models.respond.ErrorType
import ru.dezerom.domain.models.respond.RespondModel
import ru.dezerom.dto.common.StringDTO
import ru.dezerom.dto.context.SaveContextDTO
import ru.dezerom.utils.handle
import ru.dezerom.utils.sha256Hash
import java.util.*

class ContextUseCase {

    private val authRepository by lazy { AuthRepository() }
    private val contextRepository by lazy { ContextRepository() }

    suspend fun saveContext(token: String?, dto: SaveContextDTO?): RespondModel<StringDTO> {
        val foundUserCredentials = checkToken(token) ?: return RespondModel.ErrorRespondModel(ErrorType.noAccess())

        if (dto == null || dto.name.isNullOrBlank() || dto.context.isNullOrBlank())
            return RespondModel.ErrorRespondModel(ErrorType.emptyValues())

        val richContext = RichContextModel(
            id = UUID.randomUUID(),
            authorId = foundUserCredentials.id,
            name = dto.name,
            description = dto.description ?: "",
            context = dto.context,
            contextHash = dto.context.sha256Hash()
        )

        return contextRepository.saveContext(richContext).handle(
            onSuccess = {
                if (it.body.result)
                    RespondModel.SuccessRespondModel(StringDTO(richContext.id.toString()))
                else
                    RespondModel.ErrorRespondModel(ErrorType.internal())
            },
            onError = { RespondModel.ErrorRespondModel(it.errorType) }
        )
    }


    private suspend fun checkToken(token: String?): CredentialsModel? {
        return if (token == null)
            return null
        else
            authRepository.getUserByToken(token).handle(
                onSuccess = { it.body },
                onError = { null }
            )
    }
}