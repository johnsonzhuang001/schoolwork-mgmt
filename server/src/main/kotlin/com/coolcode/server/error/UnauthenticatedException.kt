package com.coolcode.server.error

class UnauthenticatedException: RuntimeException {
    constructor() : super("Unauthenticated")
    constructor(message: String) : super(message)
}