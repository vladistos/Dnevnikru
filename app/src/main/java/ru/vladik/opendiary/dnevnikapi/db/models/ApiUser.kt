package ru.vladik.opendiary.dnevnikapi.db.models

interface ApiUser {
    var uId: Long?
    var personId: Long?
    var firstName: String?
    var lastName: String?
    var photo: String?
    var token: String
    var login: String
    var password: String
    var schoolId: Long?
    var eduGroup: Long?
}

