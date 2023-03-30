package ru.vladik.dnevnikru.ext

import ru.vladik.dnevnikru.dnevnikapi.models.extended.ExtendedDay
import ru.vladik.dnevnikru.dnevnikapi.models.v7.DayBookDay

fun DayBookDay.toExtendedDay() = ExtendedDay(date, hasImportantWork, lessons, messengerEntryPoint)