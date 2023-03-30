package ru.vladik.dnevnikru.dnevnikapi.models.v7

import ru.vladik.dnevnikru.dnevnikapi.models.extended.ExtendedWeek
import java.util.*

data class Week(
    val days: List<DayBookDay>? = null,
    val firstWeekDayDate: Date? = null,
    val homeworksCount: Int? = null,
    val id: String? = null,
    val lastWeekDayDate: Date? = null
) {
    fun extend() = ExtendedWeek(days, firstWeekDayDate, homeworksCount, id, lastWeekDayDate)
}