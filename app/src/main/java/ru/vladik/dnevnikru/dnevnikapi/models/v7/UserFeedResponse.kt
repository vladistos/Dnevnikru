package ru.vladik.dnevnikru.dnevnikapi.models.v7

data class UserFeedResponse(
    val schedule: FeedSchedule? = null,
    val feed: List<FeedInfoPrototype>? = null,
) {

    @Suppress("UNCHECKED_CAST")
    val news: List<FeedInfo<FeedPost>>?
        get() {
            return feed?.filter { feedInfoPrototype ->
                return@filter feedInfoPrototype.type == FeedInfoType.Post
            } as List<FeedInfo<FeedPost>>?
        }
}