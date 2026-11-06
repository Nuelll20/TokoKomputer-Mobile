package com.example.tokokomputer.domain

import com.example.tokokomputer.model.LoginResult
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LoginValidatorTest {

    @Test
    fun correctUsernameAndPassword_returnsSuccess() {
        val result = LoginValidator.login(
            usernameOrEmail = "admin",
            password = "admin123"
        )

        assertTrue(result is LoginResult.Success)

        val successResult = result as LoginResult.Success

        assertEquals(
            "Admin Toko Komputer",
            successResult.user.displayName
        )
    }

    @Test
    fun correctEmailAndPassword_returnsSuccess() {
        val result = LoginValidator.login(
            usernameOrEmail = "admin@tokokomputer.id",
            password = "admin123"
        )

        assertTrue(result is LoginResult.Success)
    }

    @Test
    fun incorrectPassword_returnsError() {
        val result = LoginValidator.login(
            usernameOrEmail = "admin",
            password = "password-salah"
        )

        assertTrue(result is LoginResult.Error)
    }

    @Test
    fun emptyUsername_returnsError() {
        val result = LoginValidator.login(
            usernameOrEmail = "",
            password = "admin123"
        )

        assertTrue(result is LoginResult.Error)
    }
}