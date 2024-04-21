package ru.dezerom.domain.use_case

import ru.dezerom.data.repo.AnsweringRepository
import ru.dezerom.data.repo.AuthRepository
import ru.dezerom.data.repo.ContextRepository
import ru.dezerom.domain.models.auth.CredentialsModel
import ru.dezerom.domain.models.respond.ErrorType
import ru.dezerom.domain.models.respond.RespondModel
import ru.dezerom.dto.aswering.QuestionDTO
import ru.dezerom.dto.common.StringDTO
import ru.dezerom.utils.handle
import ru.dezerom.utils.map
import ru.dezerom.utils.toUUID

class AnsweringUseCase {

    private val authRepository by lazy { AuthRepository() }
    private val answeringRepository by lazy { AnsweringRepository() }
    private val contextRepository by lazy { ContextRepository() }

    suspend fun getAnswer(token: String?, contextId: String?, query: String?): RespondModel<StringDTO> {
        val foundCredentials = checkToken(token) ?: return RespondModel.ErrorRespondModel(ErrorType.noAccess())

        val contextIdUUID = contextId.toUUID()

        if (contextIdUUID == null || query.isNullOrBlank())
            return RespondModel.ErrorRespondModel(ErrorType.emptyValues())

        val context = contextRepository.getContextDetails(contextIdUUID).handle(
            onSuccess = { it.body },
            onError = { return RespondModel.ErrorRespondModel(ErrorType.nothingFound()) }
        )

        if (context.authorId != foundCredentials.id)
            return RespondModel.ErrorRespondModel(ErrorType.noAccess())

        return answeringRepository.getAnswer(context.context, query).map {
            StringDTO(it.body)
        }
    }

    suspend fun getAnswer(token: String?, questionDTO: QuestionDTO?): RespondModel<StringDTO> {
        checkToken(token) ?: return RespondModel.ErrorRespondModel(ErrorType.noAccess())

        if (questionDTO?.query == null || questionDTO.context == null)
            return RespondModel.ErrorRespondModel(ErrorType.emptyValues())

        return answeringRepository.getAnswer(questionDTO.context, questionDTO.query).map {
            StringDTO(it.body)
        }
    }

    private suspend fun checkToken(token: String?): CredentialsModel? {
        if (token == null) return null

        return authRepository.getUserByToken(token).handle(
            onSuccess = { it.body },
            onError = { null }
        )
    }

}
