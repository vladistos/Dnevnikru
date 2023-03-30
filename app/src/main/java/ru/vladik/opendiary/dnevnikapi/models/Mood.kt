package ru.vladik.opendiary.dnevnikapi.models

import androidx.annotation.ColorRes
import com.google.gson.annotations.SerializedName
import ru.vladik.opendiary.R

enum class Mood(@ColorRes val color: Int) {
    @SerializedName("Good")
    GOOD(R.color.good),
    @SerializedName("Average")
    AVERAGE(R.color.average),
    @SerializedName("Bad")
    BAD(R.color.bad);
}