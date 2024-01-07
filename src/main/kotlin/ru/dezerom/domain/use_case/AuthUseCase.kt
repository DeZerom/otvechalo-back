package ru.dezerom.domain.use_case

import ru.dezerom.data.repo.AuthRepository
import ru.dezerom.domain.models.auth.CredentialsModel
import ru.dezerom.domain.models.respond.ErrorType
import ru.dezerom.domain.models.respond.RespondModel
import ru.dezerom.dto.auth.CredentialsDTO
import ru.dezerom.dto.common.BooleanDTO
import ru.dezerom.mappers.toDTO
import ru.dezerom.utils.map

class AuthUseCase {

    private val authRepository by lazy { AuthRepository() }

    suspend fun register(credentialsDTO: CredentialsDTO?): RespondModel<BooleanDTO> {
        val credentialsModel = mapCredentials(credentialsDTO)

        return if (credentialsModel != null)
            authRepository.registerUser(credentialsModel).map { it.body.toDTO() }
        else
            RespondModel.ErrorRespondModel(ErrorType.WRONG_DATA)
    }

    private fun mapCredentials(credentialsDTO: CredentialsDTO?): CredentialsModel? {
        return if (credentialsDTO?.login != null && credentialsDTO.password != null)
            CredentialsModel(credentialsDTO.login, credentialsDTO.password)
        else
            null
    }

}
