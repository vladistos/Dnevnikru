package ru.vladik.opendiary.dnevnikapi.models.extended

import ru.vladik.opendiary.dnevnikapi.models.v2.LessonV2
import ru.vladik.opendiary.dnevnikapi.models.v2.MarkV2
import ru.vladik.opendiary.dnevnikapi.models.v7.DaybookLessonNoPayment
import ru.vladik.opendiary.dnevnikapi.models.v7.MessengerEntryPoint
import ru.vladik.opendiary.ext.toExtendedLesson
import java.util.*

class ExtendedDay(val date: Date? = null,
                  val hasImportantWork: Boolean = false,
                  val lessons: List<DaybookLessonNoPayment>? = null,
                  val messengerEntryPoint: MessengerEntryPoint? = null,
                  ) {
    fun getExtendedLessons(lessonsV2: Map<Long, LessonV2>? = null,
                           marksV2: Map<Long, ArrayList<MarkV2>>? = null): MutableList<ExtendedLesson>?
    = lessons?.map { lesson ->
        lesson.toExtendedLesson(lessonsV2?.get(lesson.id), marksV2?.get(lesson.id))
    }?.toMutableList()
}