package com.example.taskn18.users.api

import com.squareup.moshi.Json

data class ErrorResponse(
    @Json(name = "error") val error: String
)