package ru.vladik.dnevnikru.ext

import ru.vladik.dnevnikru.dnevnikapi.models.extended.ExtendedDay
import ru.vladik.dnevnikru.dnevnikapi.models.v7.DayBookDay

fun Iterable<DayBookDay>.toExtendedDaysArray() = this.map { it ->
    ExtendedDay(it.date, it.hasImportantWork, it.lessons, it.messengerEntryPoint)
}