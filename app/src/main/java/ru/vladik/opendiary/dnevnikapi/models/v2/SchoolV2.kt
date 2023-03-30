package ru.vladik.opendiary.dnevnikapi.models.v2

import com.google.gson.annotations.SerializedName

data class SchoolV2(
    val fullName: String? = null,
    var avatarSmall: String? = null,
    var city: String? = null,
    var municipality: String? = null,
    var markType: String? = null,
    var name: String? = null,
    var educationType: String? = null,
    val tsoRegionTreePath: String? = null,
    val regionId: Long? = null,
    var timeZone: Long? = null,
    var id: Long? = null,
    var tsoCityId: Long? = null,
    @SerializedName("uses_avg") val usesAvg: Boolean? = null,
    @SerializedName("uses_weighted_avg") var usesWeightedAvg: Boolean? = null
)