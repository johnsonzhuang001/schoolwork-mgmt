package com.coolcode.server.error

class NotFoundException: RuntimeException {
    constructor(): super("Not found")
    constructor(message: String): super(message)
}