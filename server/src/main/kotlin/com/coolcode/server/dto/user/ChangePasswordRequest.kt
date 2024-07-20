package com.coolcode.server.dto.user

import com.fasterxml.jackson.annotation.JsonProperty

data class ChangePasswordRequest(
    @JsonProperty("password")
    val password: String
)
