package ru.vladik.opendiary.dnevnikapi.models.v7

import java.util.*

data class DayBookDay(
    val date: Date? = null,
    val hasImportantWork: Boolean = false,
    val lessons: List<DaybookLessonNoPayment>? = null,
    val messengerEntryPoint: MessengerEntryPoint? = null
)