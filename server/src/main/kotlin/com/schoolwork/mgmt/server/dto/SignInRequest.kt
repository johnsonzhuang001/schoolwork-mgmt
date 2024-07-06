package com.schoolwork.mgmt.server.dto

data class SignInRequest(
    val username: String,
    val password: String,
)