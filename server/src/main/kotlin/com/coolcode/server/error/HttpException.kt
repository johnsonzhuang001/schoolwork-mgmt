package com.coolcode.server.error

import org.springframework.http.HttpStatus

class HttpException: RuntimeException {
    private val status: HttpStatus

    constructor(message: String): super(message) {
        status = HttpStatus.INTERNAL_SERVER_ERROR
    }

    constructor(status: HttpStatus, message: String): super(message) {
        this.status = status
    }

    constructor(status: HttpStatus, cause: Throwable): super(cause) {
        this.status = status
    }

    fun getStatus(): HttpStatus = status
}