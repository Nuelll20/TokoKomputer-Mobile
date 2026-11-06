package com.example.tokokomputer.domain

import com.example.tokokomputer.model.LoggedInUser
import com.example.tokokomputer.model.LoginResult

object LoginValidator {

    private const val DEMO_USERNAME = "admin"
    private const val DEMO_EMAIL = "admin@tokokomputer.id"
    private const val DEMO_PASSWORD = "admin123"

    fun login(
        usernameOrEmail: String,
        password: String
    ): LoginResult {
        val normalizedUsername = usernameOrEmail.trim()

        if (normalizedUsername.isBlank()) {
            return LoginResult.Error(
                message = "Username atau email wajib diisi."
            )
        }

        if (password.isBlank()) {
            return LoginResult.Error(
                message = "Password wajib diisi."
            )
        }

        if (password.length < 6) {
            return LoginResult.Error(
                message = "Password minimal terdiri dari 6 karakter."
            )
        }

        val usernameIsValid =
            normalizedUsername.equals(DEMO_USERNAME, ignoreCase = true) ||
                    normalizedUsername.equals(DEMO_EMAIL, ignoreCase = true)

        val passwordIsValid = password == DEMO_PASSWORD

        if (!usernameIsValid || !passwordIsValid) {
            return LoginResult.Error(
                message = "Username, email, atau password tidak sesuai."
            )
        }

        return LoginResult.Success(
            user = LoggedInUser(
                id = "USER-001",
                username = DEMO_USERNAME,
                displayName = "Admin Toko Komputer"
            )
        )
    }
}