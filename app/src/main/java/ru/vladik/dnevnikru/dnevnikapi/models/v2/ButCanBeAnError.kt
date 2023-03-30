package ru.vladik.dnevnikru.dnevnikapi.models.v2

import com.google.gson.annotations.SerializedName

abstract class ButCanBeAnError (

    @SerializedName("type")
    val errType: ErrorType? = null,
    val description: String? = null
)

