package ru.vladik.opendiary.dnevnikapi.models.v7

import ru.vladik.opendiary.dnevnikapi.models.ReactionType

data class FeedPostLikes(
    val countWithoutUser: Int? = null,
    val externalId: String? = null,
    val iconsVotes: List<ReactionType>? = null,
    val userVote: ReactionType? = null
)