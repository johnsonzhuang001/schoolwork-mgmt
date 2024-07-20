package com.coolcode.server.dto

data class OkDto(
    val message: String
) {
    constructor(): this(
        message = "OK"
    )
}
