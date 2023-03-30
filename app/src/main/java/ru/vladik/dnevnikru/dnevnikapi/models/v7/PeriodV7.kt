package ru.vladik.dnevnikru.dnevnikapi.models.v7

import com.google.gson.annotations.SerializedName

data class PeriodV7 (
    @SerializedName("isCurrent")
    val isCurrent: Boolean? = null,
    val dateFinish: Long? = null,
    val dateStart: Long? = null,
    val id: Long? = null,
    val number: Long? = null,
    val studyYear: Long? = null,
    val type: String? = null,
)