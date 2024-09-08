package com.coolcode.server.utils

import kotlin.random.Random

class PasswordUtils {
    companion object {
        fun generateRandomPassword(length: Int): String {
            val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9') + listOf('@', '.', '#', '$', '!', '%', '*', '?', '&', '^')
            return (1..length)
                .map { Random.nextInt(0, charPool.size).let { charPool[it] } }
                .joinToString("")
        }

        fun isValidPassword(password: String): Boolean {
            val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@.#$!%*?&^])[A-Za-z\\d@.#$!%*?&]{8,16}$")
            return password.matches(regex)
        }
    }
}