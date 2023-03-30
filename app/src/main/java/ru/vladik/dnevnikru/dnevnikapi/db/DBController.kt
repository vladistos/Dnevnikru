package ru.vladik.dnevnikru.dnevnikapi.db

import android.content.Context
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async

abstract class DBController(private val context: Context) {

    fun method() = GlobalScope.async {

    }
}