package ru.vladik.dnevnikru.dnevnikapi.models.v2

import com.google.gson.annotations.SerializedName

enum class ErrorType {
    @SerializedName("invalidToken")
    INVALID_TOKEN
}