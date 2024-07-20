package com.coolcode.server.dto

data class SignInRequest(
    val username: String,
    val password: String,
)