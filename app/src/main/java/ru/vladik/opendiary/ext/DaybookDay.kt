package ru.vladik.opendiary.ext

import ru.vladik.opendiary.dnevnikapi.models.extended.ExtendedDay
import ru.vladik.opendiary.dnevnikapi.models.v7.DayBookDay

fun Iterable<DayBookDay>.toExtendedDaysArray() = this.map { it ->
    ExtendedDay(it.date, it.hasImportantWork, it.lessons, it.messengerEntryPoint)
}