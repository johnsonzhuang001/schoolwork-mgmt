package com.coolcode.server.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class JwtRequest(
    @JsonProperty("accessToken") val accessToken: String?,
)
