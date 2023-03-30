package ru.vladik.dnevnikru.dnevnikapi.models.v2

data class UserContextV2(
    val shortName: String? = null,
    val userId: Long? = null,
    var personId: Long? = null,
    val splitId: Any? = null,
    val schoolIds: List<Long>? = null,
    var groupIds: MutableList<Long>? = null,
    val roles: List<String>? = null,
    var children: MutableList<String>? = null,
    val schools: List<SchoolV2>? = null,
    val eduGroups: List<EduGroupV2>? = null
) : ButCanBeAnError()
