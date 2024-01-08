package ru.dezerom.utils

import ru.dezerom.domain.models.call_result.CallResult
import ru.dezerom.domain.models.common.ActionResult
import ru.dezerom.domain.models.respond.ErrorType
import ru.dezerom.domain.models.respond.RespondModel

suspend fun makeAction(
    action: suspend () -> Boolean?,
    onError: (Throwable) -> CallResult<ActionResult> = { CallResult.Error(ErrorType.internal()) }
): CallResult<ActionResult> {
    return try {
        CallResult.Success(ActionResult(action() == true))
    } catch (t: Throwable) {
        onError(t)
    }
}

suspend fun <T> makeCall(
    call: suspend () -> T?,
    onNull: () -> CallResult<T>,
    onError: (Throwable) -> CallResult<T> = { CallResult.Error(ErrorType.internal()) }
): CallResult<T> {
    return try {
        val result = call()

        if (result != null)
            CallResult.Success(result)
        else
            onNull()
    } catch (t: Throwable) {
        onError(t)
    }
}

fun <T, R> CallResult<T>.map(
    onSuccess: (CallResult.Success<T>) -> R
): RespondModel<R> {
    return when (this) {
        is CallResult.Success ->
            RespondModel.SuccessRespondModel(onSuccess(this))

        is CallResult.Error ->
            RespondModel.ErrorRespondModel(this.errorType)
    }
}

inline fun <T, R> CallResult<T>.handle(
    onSuccess: (CallResult.Success<T>) -> R,
    onError: (CallResult.Error) -> R
): R {
    return when (this) {
        is CallResult.Success -> onSuccess(this)

        is CallResult.Error -> onError(this)
    }
}
