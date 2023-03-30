package ru.vladik.dnevnikru.dnevnikapi.models.v7

import java.util.*

data class DaybookLessonNoPayment(
    val comment: LessonComment? = null,
    val date: Long? = null,
    val endDateTime: Date? = null,
    val hasAttachment: Boolean? = null,
    val hours: Hours? = null,
    val id: Long? = null,
    val importantWorks: List<String>? = null,
    val isCanceled: Boolean? = null,
    val isEmpty: Boolean = false,
    val number: Int? = null,
    val place: String? = null,
    val startDateTime: Date? = null,
    val subject: SubjectV7? = null,
    val theme: String? = null,
    val teacher: LessonTeacher? = null
)