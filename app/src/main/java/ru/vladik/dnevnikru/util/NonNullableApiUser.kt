package ru.vladik.dnevnikru.util

import ru.vladik.dnevnikru.dnevnikapi.db.models.ApiUser
import ru.vladik.dnevnikru.dnevnikapi.db.models.NotNullableApiUser

class NonNullableApiUserImpl(
    override var uId: Long,
    override var personId: Long,
    override var firstName: String,
    override var lastName: String,
    override var photo: String?,
    override var token: String,
    override var login: String,
    override var password: String,
    override var schoolId: Long,
    override var eduGroup: Long
) : NotNullableApiUser {
    constructor(apiUser: ApiUser) : this(
        requireNotNull(apiUser.uId),
        requireNotNull(apiUser.personId),
        apiUser.firstName.orEmpty(),
        apiUser.lastName.orEmpty(),
        apiUser.photo,
        apiUser.token,
        apiUser.login,
        apiUser.password,
        requireNotNull(apiUser.schoolId),
        requireNotNull(apiUser.eduGroup)
    )
}