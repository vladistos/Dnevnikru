package ru.vladik.dnevnikru.dnevnikapi.models.v7

class FeedInfo<T> (
    type: FeedInfoType? = null,
    timeStamp: Long? = null,
    val content: T? = null
) : FeedInfoPrototype(type, timeStamp)