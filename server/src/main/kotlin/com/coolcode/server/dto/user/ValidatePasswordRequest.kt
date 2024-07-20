package com.coolcode.server.dto.user

import com.fasterxml.jackson.annotation.JsonProperty

data class ValidatePasswordRequest(
    @JsonProperty("password")
    val password: String
)
