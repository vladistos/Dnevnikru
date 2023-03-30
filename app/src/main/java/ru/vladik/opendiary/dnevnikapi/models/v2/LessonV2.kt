package ru.vladik.opendiary.dnevnikapi.models.v2

data class LessonV2(
    val works: List<WorkV2>? = null
) : LessonModelV2() {
    val homework: String?
        get() {
            return works?.find { workV2 -> workV2.type == WorkType.HOMEWORK }?.text
        }

}