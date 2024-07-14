package com.schoolwork.mgmt.server.error

class UnauthenticatedException: RuntimeException {
    constructor() : super("Unauthenticated")
    constructor(message: String) : super(message)
}