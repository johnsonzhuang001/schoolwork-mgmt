package com.schoolwork.mgmt.server.security

enum class UserRole(val value: String, val privileges: List<Privilege>) {
    ADMIN("ADMIN", listOf(
        Privilege.READ_ASSIGNMENT,
        Privilege.WRITE_ASSIGNMENT,
        Privilege.SCORE_ASSIGNMENT,
    )),
    MENTOR("MENTOR", listOf(
        Privilege.READ_ASSIGNMENT,
        Privilege.SCORE_ASSIGNMENT,
    )),
    STUDENT("STUDENT", listOf(
        Privilege.READ_ASSIGNMENT,
        Privilege.SUBMIT_ASSIGNMENT,
    ));
}