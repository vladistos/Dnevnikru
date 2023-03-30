package ru.vladik.opendiary.dnevnikapi.models.v2

import com.google.gson.annotations.SerializedName

enum class WorkType {
    @SerializedName("LessonExamination")
    LESSON_EXAMINATION,
    @SerializedName("LessonTestWork")
    LESSON_TEST_WORK,
    @SerializedName("DefaultNewLessonWork")
    DEFAULT_NEW_LESSON_WORK,
    @SerializedName("CommonWork")
    COMMON_WORK,
    @SerializedName("PeriodMark")
    PERIOD_MARK,
    @SerializedName("LessonComposition")
    LESSON_COMPOSITION,
    @SerializedName("LessonByHeart")
    LESSON_BY_HEART,
    @SerializedName("Homework")
    HOMEWORK
}