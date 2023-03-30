package ru.vladik.dnevnikru.dnevnikapi.models.extended

import ru.vladik.dnevnikru.dnevnikapi.models.v2.LessonV2
import ru.vladik.dnevnikru.dnevnikapi.models.v2.MarkV2
import ru.vladik.dnevnikru.dnevnikapi.models.v7.DayBookDay
import ru.vladik.dnevnikru.ext.toExtendedDay
import java.util.*

class ExtendedWeek(var days: List<DayBookDay>? = null,
                   val firstWeekDayDate: Date? = null,
                   val homeworksCount: Int? = null,
                   val id: String? = null,
                   val lastWeekDayDate: Date? = null,
                   var lessonsV2: Map<Long, LessonV2>? = null,
                   var marksV2: Map<Long, ArrayList<MarkV2>>? = null) {
    val extendedDays
        get() = days?.map { day -> day.toExtendedDay() }
}