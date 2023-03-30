package ru.vladik.dnevnikru.dnevnikapi.models.v7


data class UserContextV7 (
    val contextPersons: List<ContextPersonV7>? = null,
    val firebaseExperiments: List<String>? = null,
    val info: InfoV7? = null,
)