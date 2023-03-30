package ru.vladik.dnevnikru.dnevnikapi.models.v7

import ru.vladik.dnevnikru.dnevnikapi.models.Mood

data class MessengerEntryPoint(
    val chatInfo: ChatInfoJid? = null,
    val isPointForFreeSubscription: Boolean = false,
    val markMood: Mood? = null,
    val text: String?
)