package ru.vladik.dnevnikru.dnevnikapi.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.vladik.dnevnikru.dnevnikapi.models.v7.ContextPersonV7
import java.io.Serializable

@Entity(tableName = "user")
data class ApiUserImpl (
    @ColumnInfo(name = "uid") override var uId: Long? = null,
    @PrimaryKey @ColumnInfo(name = "person_id") override var personId: Long? = null,
    @ColumnInfo(name = "first_name") override var firstName: String? = null,
    @ColumnInfo(name = "last_name") override var lastName: String? = null,
    @ColumnInfo(name = "photo") override var photo: String? = null,
    @ColumnInfo(name = "token") override var token: String,
    @ColumnInfo(name = "login") override var login: String,
    @ColumnInfo(name = "password") override var password: String,
    @ColumnInfo(name = "school_id") override var schoolId: Long? = null,
    @ColumnInfo(name = "edu_group") override var eduGroup: Long? = null
) : Serializable, ApiUser {
    val primaryKey: Long?
        get() = personId

    fun refreshData(contextPerson: ContextPersonV7) {
        uId = contextPerson.userId
        personId = contextPerson.personId
        firstName = contextPerson.firstName
        lastName = contextPerson.lastName
        photo = contextPerson.avatarUrl
        schoolId = contextPerson.school?.id
        eduGroup = contextPerson.group?.id ?: -1L
    }

    val initials : String?
        get() {
            return this.firstName?.first()?.plus(this.lastName?.first().toString())
        }

    val fullName: String?
        get() {
            return this.firstName?.plus(" ")?.plus(this.lastName)
        }

    constructor(contextPerson: ContextPersonV7, token: String, login: String, password: String) : this (
        contextPerson.userId!!,
        contextPerson.personId!!,
        contextPerson.firstName,
        contextPerson.lastName,
        contextPerson.avatarUrl,
        token, login, password,
        contextPerson.schoolId,
        contextPerson.group?.id ?: -1L
    )

}

