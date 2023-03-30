package ru.vladik.opendiary.dnevnikapi.models

import com.google.gson.annotations.SerializedName

enum class ReactionType {
    @SerializedName("Like")
    Like,
    @SerializedName("Heart")
    Heart,
    @SerializedName("Ok")
    Ok,
    @SerializedName("Laughing")
    Laughing,
    @SerializedName("Surprised")
    Surprised,
    @SerializedName("Sad")
    Sad,
    @SerializedName("Angry")
    Angry
}