package com.schoolwork.mgmt.server.error

class UnauthorizedException: RuntimeException {
    constructor() : super("Unauthorized")
    constructor(message: String) : super(message)
}