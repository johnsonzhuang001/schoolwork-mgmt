package com.schoolwork.mgmt.server.dto

data class OkDto(
    val message: String
) {
    constructor(): this(
        message = "OK"
    )
}
