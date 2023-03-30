package ru.vladik.opendiary.dnevnikapi.models.v7

import ru.vladik.opendiary.dnevnikapi.models.Mood

data class MessengerEntryPoint(
    val chatInfo: ChatInfoJid? = null,
    val isPointForFreeSubscription: Boolean = false,
    val markMood: Mood? = null,
    val text: String?
)