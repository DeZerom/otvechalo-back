package ru.dezerom.utils

import ru.dezerom.domain.models.call_result.CallResult
import ru.dezerom.domain.models.respond.RespondModel

fun <T, R>CallResult<T>.map(
    onSuccess: (CallResult.Success<T>) -> R
): RespondModel<R> {
    return when (this) {
        is CallResult.Success ->
            RespondModel.SuccessRespondModel(onSuccess(this))

        is CallResult.Error ->
            RespondModel.ErrorRespondModel(this.errorType)
    }
}
