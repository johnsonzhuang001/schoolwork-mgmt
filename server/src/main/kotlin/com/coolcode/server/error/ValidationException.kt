package com.coolcode.server.error

class ValidationException: RuntimeException {
    constructor() : super("Invalid Request")
    constructor(message: String) : super(message)
}