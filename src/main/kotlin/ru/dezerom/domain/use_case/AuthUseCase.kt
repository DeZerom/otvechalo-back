package ru.dezerom.domain.use_case

import ru.dezerom.data.repo.AuthRepository
import ru.dezerom.domain.models.auth.CredentialsModel
import ru.dezerom.domain.models.respond.ErrorType
import ru.dezerom.domain.models.respond.RespondModel
import ru.dezerom.dto.auth.CredentialsDTO
import ru.dezerom.dto.common.BooleanDTO
import ru.dezerom.dto.common.StringDTO
import ru.dezerom.mappers.toDTO
import ru.dezerom.utils.handle
import ru.dezerom.utils.map
import ru.dezerom.utils.sha256Hash
import java.util.*

class AuthUseCase {

    private val authRepository by lazy { AuthRepository() }

    suspend fun authorize(credentialsDTO: CredentialsDTO?): RespondModel<StringDTO> {
        if (credentialsDTO?.login == null || credentialsDTO.password == null)
            return RespondModel.ErrorRespondModel(ErrorType.emptyValues())

        val foundCredentials = authRepository.getUser(credentialsDTO.login).handle(
            onSuccess = { it.body },
            onError = { return RespondModel.ErrorRespondModel(it.errorType) }
        )

        val foundToken = authRepository.getToken(foundCredentials.id).handle(
            onSuccess = { it.body },
            onError = { null }
        )

        if (foundToken != null)
            return RespondModel.SuccessRespondModel(StringDTO(foundToken))

        return if (foundCredentials.password == (credentialsDTO.password + foundCredentials.salt).sha256Hash())
            authRepository.createToken(foundCredentials.id).map { StringDTO(it.body) }
        else
            RespondModel.ErrorRespondModel(ErrorType.WrongData(ErrorType.Reasons.WRONG_PASS))
    }

    suspend fun register(credentialsDTO: CredentialsDTO?): RespondModel<BooleanDTO> {
        val credentialsModel = mapCredentials(credentialsDTO)

        return if (credentialsModel != null) {
            if (authRepository.isUserExists(credentialsModel.login))
                RespondModel.ErrorRespondModel(ErrorType.WrongData(ErrorType.Reasons.USER_EXISTS))
            else
                authRepository.registerUser(credentialsModel).map { it.body.toDTO() }
        } else {
            RespondModel.ErrorRespondModel(ErrorType.emptyValues())
        }
    }

    private fun mapCredentials(credentialsDTO: CredentialsDTO?): CredentialsModel? {
        if (credentialsDTO?.login == null || credentialsDTO.password == null)
            return null

        val salt = UUID.randomUUID().toString()
        val hashedPassword = (credentialsDTO.password + salt).sha256Hash()

        return CredentialsModel(
            id = UUID.randomUUID(),
            login = credentialsDTO.login,
            password = hashedPassword,
            salt = salt
        )
    }

}
