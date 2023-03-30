package ru.vladik.dnevnikru.ext

import androidx.fragment.app.Fragment

val Fragment.isNotAdded
    get() = !this.isAdded