package com.schoolwork.mgmt.server.dto.user

import com.fasterxml.jackson.annotation.JsonProperty

data class ValidatePasswordRequest(
    @JsonProperty("password")
    val password: String
)
