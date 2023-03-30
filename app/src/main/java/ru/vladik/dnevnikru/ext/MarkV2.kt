package ru.vladik.dnevnikru.ext

import ru.vladik.dnevnikru.dnevnikapi.models.v2.MarkV2

fun Iterable<MarkV2>.toLessonIdValueMap(): HashMap<Long, ArrayList<MarkV2>> {
    val map = HashMap<Long, ArrayList<MarkV2>>()
    for (mark in this) {
        if (mark.lesson.isNull()) {
            continue
        }
        val lesson = requireNotNull(mark.lesson)
        if (map.get(lesson) == null) {
            map[lesson] = ArrayList<MarkV2>().apply { add(mark) }
        } else {
            requireNotNull(map[lesson]).add(mark)
        }
    }
    return map
}