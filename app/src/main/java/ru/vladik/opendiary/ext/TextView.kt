package ru.vladik.opendiary.ext

import android.view.View
import android.widget.TextView

fun TextView.hideIfNullShowElse() {
    if (text.isNullOrEmpty()) {
        visibility = View.GONE
    } else {
        visibility = View.VISIBLE
    }
}