package ru.vladik.dnevnikru.dnevnikapi.models.v7

import ru.vladik.dnevnikru.func.getInitials

data class LessonTeacher(
    val avatarUrl: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val middleName: String? = null,
    val role: String? = null
) {
    val fullName: String
        get() = "$lastName $firstName $middleName"

    val initials: String
        get() = getInitials(firstName, middleName)
}