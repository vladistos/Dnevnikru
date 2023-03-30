package ru.vladik.opendiary.dnevnikapi.models.v2

import ru.vladik.opendiary.ext.toIdValueMap

data class RecentMarksResp(val marks: List<MarkV2>, val subjects: List<SubjectV2>,
                           val lessons: List<LessonNotFullV2>, val works: List<WorkV2>) {

    data class ExtendedMark(val markV2: MarkV2, val subject: SubjectV2?, val lesson: LessonNotFullV2?,
                            val work: WorkV2?)

    val extendedMarks : List<ExtendedMark>
        get() {
            val subjectsMap = subjects.toIdValueMap()
            val lessonsMap = lessons.toIdValueMap()
            val worksMap = works.toIdValueMap()
            val extendedMarks = ArrayList<ExtendedMark>()
            for (mark in marks) {
                var subject : SubjectV2? = null
                val lesson = lessonsMap[mark.lesson]
                val work = worksMap[mark.work]
                work?.subjectId?.let { subject = subjectsMap[it] }
                lesson?.subjectId?.let { subject = subjectsMap[it] }
                extendedMarks.add(ExtendedMark(mark, subject, lesson, work))
            }

            return extendedMarks
        }
}

