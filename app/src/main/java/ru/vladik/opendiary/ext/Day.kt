package ru.vladik.opendiary.ext

import ru.vladik.opendiary.dnevnikapi.models.extended.ExtendedDay
import ru.vladik.opendiary.dnevnikapi.models.v7.DayBookDay

fun DayBookDay.toExtendedDay() = ExtendedDay(date, hasImportantWork, lessons, messengerEntryPoint)