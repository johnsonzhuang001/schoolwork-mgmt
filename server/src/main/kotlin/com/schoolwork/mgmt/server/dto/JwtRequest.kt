package com.schoolwork.mgmt.server.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class JwtRequest(
    @JsonProperty("accessToken") val accessToken: String?,
)
