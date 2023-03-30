package ru.vladik.opendiary

import android.util.Log

fun logClass(context: Any, string: String) = Log.d(context::class.simpleName, string)

fun Any?.logSelf(what: Any = this.toString()) = logClass(this ?: "null", what.toString())