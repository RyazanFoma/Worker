package com.pushe.worker.logup.model

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class LogUpViewModelTest {

    private val logUpViewModel = LogUpViewModel()

    @Test
    fun testIsVerified() {
        assertFalse(logUpViewModel.isVerified("1234"), "password verification")
        assertTrue(logUpViewModel.isVerified(""), "password verification")
    }
}