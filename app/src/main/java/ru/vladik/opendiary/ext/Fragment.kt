package ru.vladik.opendiary.ext

import androidx.fragment.app.Fragment

val Fragment.isNotAdded
    get() = !this.isAdded