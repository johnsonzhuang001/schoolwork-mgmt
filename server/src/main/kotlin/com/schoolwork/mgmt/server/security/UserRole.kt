package com.schoolwork.mgmt.server.security

enum class UserRole(val value: String, val privileges: List<Privilege>) {
    MENTOR("MENTOR", listOf(
        Privilege.READ_ASSIGNMENT,
        Privilege.SCORE_ASSIGNMENT,
    )),
    STUDENT("STUDENT", listOf(
        Privilege.READ_ASSIGNMENT,
    ));
}