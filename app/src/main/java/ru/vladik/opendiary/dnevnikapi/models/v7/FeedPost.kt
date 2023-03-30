package ru.vladik.opendiary.dnevnikapi.models.v7

import ru.vladik.opendiary.ext.firstOrEmpty


data class FeedPost(
    val id: Long? = null,
    val eventKey: String? = null,
    val topicEventKey: String? = null,
    val topicLogoUrl: String? = null,
    val eventUrl: String? = null,
    val eventSign: String? = null,
    val title: String? = null,
    val subtitle: String? = null,
    val text: String? = null,
    val createdDateTime: String? = null,
    val isReaded: String? = null,
    val commentsCount: Int? = null,
    val authorImageUrl: String? = null,
    val authorFirstName: String? = null,
    val authorMiddleName: String? = null,
    val authorLastName: String? = null,
    val authorName: String? = null,
    val type: FeedInfoType? = null,
    val previewUrl: String? = null,
    val attachmentFiles: List<Attachment>? = null,
    val isNew: Boolean = false,
    val likes: FeedPostLikes? = null,
    val unsubscribeIsPossible: Boolean = false,
    val publicClubId: Long? = null,
    val messengerEntryPoint: MessengerEntryPoint? = null
) {
    fun getAuthorLastNameAndInitials() = "$authorLastName ${authorFirstName.firstOrEmpty()} ${authorMiddleName.firstOrEmpty()}"

}
