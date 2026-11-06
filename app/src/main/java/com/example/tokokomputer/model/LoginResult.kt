package com.example.tokokomputer.model

data class LoggedInUser(
    val id: String,
    val username: String,
    val displayName: String
)

sealed class LoginResult {

    data class Success(
        val user: LoggedInUser
    ) : LoginResult()

    data class Error(
        val message: String
    ) : LoginResult()
}