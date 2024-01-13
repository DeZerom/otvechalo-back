package ru.dezerom.domain.use_case

import ru.dezerom.data.repo.AuthRepository
import ru.dezerom.data.repo.ContextRepository
import ru.dezerom.domain.models.auth.CredentialsModel
import ru.dezerom.domain.models.context.LightWeightContextModel
import ru.dezerom.domain.models.context.RichContextModel
import ru.dezerom.domain.models.respond.ErrorType
import ru.dezerom.domain.models.respond.RespondModel
import ru.dezerom.dto.common.BooleanDTO
import ru.dezerom.dto.common.StringDTO
import ru.dezerom.dto.context.ContextLightWeightDTO
import ru.dezerom.dto.context.RichContextDTO
import ru.dezerom.dto.context.SaveContextDTO
import ru.dezerom.mappers.toDTO
import ru.dezerom.utils.handle
import ru.dezerom.utils.map
import ru.dezerom.utils.sha256Hash
import java.util.*

class ContextUseCase {

    private val authRepository by lazy { AuthRepository() }
    private val contextRepository by lazy { ContextRepository() }

    suspend fun getContextDetails(token: String?, id: String?): RespondModel<RichContextDTO> {
        val foundCredentials = checkToken(token) ?: return RespondModel.ErrorRespondModel(ErrorType.noAccess())

        val idUUID = checkContextId(id)
            ?: return RespondModel.ErrorRespondModel(ErrorType.WrongData("Invalid id"))

        return contextRepository.getContextDetails(idUUID).handle(
            onSuccess = {
                if (it.body.authorId == foundCredentials.id)
                    RespondModel.SuccessRespondModel(it.body.toDTO())
                else
                    RespondModel.ErrorRespondModel(ErrorType.noAccess())
            },
            onError = { RespondModel.ErrorRespondModel(it.errorType) }
        )
    }

    suspend fun getLightWeightContexts(token: String?): RespondModel<List<ContextLightWeightDTO>> {
        val foundCredentials = checkToken(token) ?: return RespondModel.ErrorRespondModel(ErrorType.noAccess())

        return contextRepository.getContextsLightWeight(foundCredentials.id).map {
            it.body.map(LightWeightContextModel::toDTO)
        }
    }

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

    suspend fun updateContext(token: String?, id: String?, dto: SaveContextDTO?): RespondModel<BooleanDTO> {
        val foundCredentials = checkToken(token) ?: return RespondModel.ErrorRespondModel(ErrorType.noAccess())

        val idUUID = checkContextId(id)
            ?: return RespondModel.ErrorRespondModel(ErrorType.WrongData("Invalid id"))

        if (dto == null)
            return RespondModel.ErrorRespondModel(ErrorType.emptyValues())

        val context = contextRepository.getContextDetails(idUUID).handle(
            onSuccess = { it.body },
            onError = { return RespondModel.ErrorRespondModel(it.errorType) }
        )

        if (context.authorId != foundCredentials.id)
            return RespondModel.ErrorRespondModel(ErrorType.noAccess())

        val hasChanges = context.name != dto.name || context.description != dto.description
                || context.context != dto.context

        if (!hasChanges)
            return RespondModel.SuccessRespondModel(BooleanDTO(true))

        return contextRepository.updateContext(
            newContext = context.copy(
                name = dto.name ?: context.name,
                description = dto.description ?: context.description,
                context = dto.context ?: context.context,
                contextHash = dto.context?.sha256Hash() ?: context.context.sha256Hash()
            )
        ).map { it.body.toDTO() }
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

    private fun checkContextId(id: String?): UUID? {
        return try {
            UUID.fromString(id)
        } catch (_: IllegalArgumentException) {
            null
        }
    }
}
