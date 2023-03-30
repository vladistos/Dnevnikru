package ru.vladik.dnevnikru.dnevnikapi.models.v7

import ru.vladik.dnevnikru.dnevnikapi.models.ReactionType

data class FeedPostLikes(
    val countWithoutUser: Int? = null,
    val externalId: String? = null,
    val iconsVotes: List<ReactionType>? = null,
    val userVote: ReactionType? = null
)