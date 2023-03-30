package ru.vladik.dnevnikru.dnevnikapi.models.extended

import ru.vladik.dnevnikru.dnevnikapi.models.v2.LessonV2
import ru.vladik.dnevnikru.dnevnikapi.models.v2.MarkV2
import ru.vladik.dnevnikru.dnevnikapi.models.v7.DaybookLessonNoPayment

class ExtendedLesson(val lessonV7: DaybookLessonNoPayment, val lessonV2: LessonV2?,
val marks: Iterable<MarkV2>?)

