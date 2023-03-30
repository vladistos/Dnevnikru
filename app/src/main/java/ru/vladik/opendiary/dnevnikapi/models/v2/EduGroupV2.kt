package ru.vladik.opendiary.dnevnikapi.models.v2

import ru.vladik.opendiary.dnevnikapi.models.Entity

data class EduGroupV2 (
    var type: String? = null,
    var name: String? = null,
    var fullName: String? = null,
    var status: String? = null,
    var journaltype: String? = null,
    val parallel: Int? = null,
    override val id: Long? = null,
    var timetable: Long? = null,
    var studyyear: Long? = null,
    val subjects: List<SubjectV2>? = null,
    val parentIds: List<Long>? = null
) : Entity