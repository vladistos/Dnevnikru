package ru.vladik.dnevnikru.dnevnikapi.models

data class ActivationData(
    private val birthday: String? = null,
    private val firstName: String? = null,
    private val isPhoneRequired: Boolean? = null,
    private val lastName: String? = null,
    private val middleName: String? = null,
    private val password: String? = null,
    private val personId: Long? = null,
    private val sex: String? = null,
    private val termsLink: String? = null,
    private val token: String? = null,
    private val username: String? = null,
)