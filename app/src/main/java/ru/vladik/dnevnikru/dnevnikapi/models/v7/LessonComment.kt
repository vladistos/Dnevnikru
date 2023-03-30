package ru.vladik.dnevnikru.dnevnikapi.models.v7

data class LessonComment(
    private val author: LessonTeacher? = null,
    private val date: Long? = null,
    private val text: String? = null
)