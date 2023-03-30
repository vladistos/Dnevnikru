package ru.vladik.opendiary.ext

fun String?.nullIfEmpty(): String? {
    return if (isNullOrEmpty() || replace(" ", "").isEmpty()) null else this
}

fun String?.firstOrEmpty() = if (isNullOrEmpty()) "" else first()