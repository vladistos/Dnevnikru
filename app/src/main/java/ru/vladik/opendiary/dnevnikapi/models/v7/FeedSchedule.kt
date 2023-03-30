package ru.vladik.opendiary.dnevnikapi.models.v7

data class FeedSchedule(
    val itemId: Long? = null,
    val nextLessonDate: Long? = null,
    val todayLessons: List<LessonFeed>? = null,
    val tomorrowLessons: List<LessonFeed>? = null
)