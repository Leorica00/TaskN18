package com.example.taskn18

import com.example.taskn18.users.api.RetrofitClient
import retrofit2.HttpException
import java.io.IOException
import java.util.concurrent.TimeoutException

sealed class AppError(open val message: String) {
    data class NetworkError(override val message: String) : AppError(message)
    data class HttpError(override val message: String) : AppError(message)
    data class TimeoutError(override val message: String) : AppError(message)
    data class ServerError(override val message: String) : AppError(message)
    data class ClientError(override val message: String) : AppError(message)
    data class UnknownError(override val message: String) : AppError(message)

    companion object {
        fun fromException(throwable: Throwable): AppError {
            return when (throwable) {
                is IOException -> NetworkError("Network error occurred: No Internet")
                is HttpException -> {
                    when (throwable.code()) {
                        in 400..499 -> {
                            val errorModel = throwable.response()?.errorBody()?.string()
                                ?.let { RetrofitClient.errorResponse().fromJson(it) }
                            ClientError(errorModel?.error.toString())
                        }

                        in 500..599 -> ServerError("Server error occurred")
                        else -> HttpError("Http error occurred")
                    }
                }

                is TimeoutException -> TimeoutError("Can not process task")
                else -> UnknownError("An unexpected error occurred")
            }
        }
    }
}