package ru.vladik.dnevnikru.ext

import io.getstream.avatarview.AvatarView
import io.getstream.avatarview.coil.loadImage

fun AvatarView.loadImageWithInitialsWrapper(data: String?, initials: String, onComplete: (() -> Unit)? = null) {

    loadImage(data,
        onStart = {
            avatarInitials = initials
        },
        onComplete = {
            onComplete?.invoke()
            avatarInitials = null
        }
    )
}