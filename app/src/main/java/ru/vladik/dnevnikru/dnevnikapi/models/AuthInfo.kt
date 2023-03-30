package ru.vladik.dnevnikru.dnevnikapi.models

import ru.vladik.dnevnikru.ApiConstants

data class AuthInfo(
    private val username: String? = null,
    private val password: String? = null,

    private val agreeTerms: Boolean? = null,
    private val email: String? = null,
    private val newPassword: String? = null,
    private val newPasswordRepeat: String? = null,
    private val passwordRepeat: String? = null,
    private val phone: String? = null,
    private val token: String? = null,
    private val clientId: String? = ApiConstants.API_KEY,
    private val clientSecret: String? = ApiConstants.API_SECRET,
    private val scope: String? = ApiConstants.SCOPE,
)