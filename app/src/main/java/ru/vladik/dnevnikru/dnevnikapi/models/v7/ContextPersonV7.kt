package ru.vladik.dnevnikru.dnevnikapi.models.v7

data class ContextPersonV7 (
    val avatarUrl: String? = null,
    val firstName: String? = null,
    val group: GroupV7? = null,
    val lastName: String? = null,
    val middleName: String? = null,
    val personId: Long? = null,
    val reportingPeriodGroup: ReportingPeriodGroupV7? = null,
    val school: SchoolV7? = null,
    val schoolId: Long? = null,
    val sex: String? = null,
    val userId: Long? = null,
)