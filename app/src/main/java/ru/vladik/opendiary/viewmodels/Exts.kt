package ru.vladik.opendiary.viewmodels

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

fun CoroutineScope.launchAsync(start: CoroutineStart = CoroutineStart.DEFAULT,
                               block: suspend CoroutineScope.() -> Unit) =
    this.launch(Dispatchers.IO, start, block)