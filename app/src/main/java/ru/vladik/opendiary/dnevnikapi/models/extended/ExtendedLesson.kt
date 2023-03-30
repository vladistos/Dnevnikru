package ru.vladik.opendiary.dnevnikapi.models.extended

import ru.vladik.opendiary.dnevnikapi.models.v2.LessonV2
import ru.vladik.opendiary.dnevnikapi.models.v2.MarkV2
import ru.vladik.opendiary.dnevnikapi.models.v7.DaybookLessonNoPayment

class ExtendedLesson(val lessonV7: DaybookLessonNoPayment, val lessonV2: LessonV2?,
val marks: Iterable<MarkV2>?)

