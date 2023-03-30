package ru.vladik.dnevnikru.dnevnikapi.models.v7

data class LessonFeed(
    private val comment: LessonComment? = null,
    private val endTime: Long? = null,
    private val hours: Hours? = null,
    private val id: Long? = null,
    private val isCanceled: Boolean? = null,
    private val number: Int? = null,
    private val place: String? = null,
    private val startTime: Long? = null,
    private val subject: SubjectV7? = null
)