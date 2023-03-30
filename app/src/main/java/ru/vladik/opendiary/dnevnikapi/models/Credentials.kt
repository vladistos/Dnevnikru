package ru.vladik.opendiary.dnevnikapi.models

data class Credentials(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val scope: String? = null,
    val userId: Long? = null,
)