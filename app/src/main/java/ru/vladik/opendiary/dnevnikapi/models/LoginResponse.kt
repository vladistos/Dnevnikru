package ru.vladik.opendiary.dnevnikapi.models

data class LoginResponse(
    val activationData: ActivationData? = null,
    val credentials: Credentials? = null,
    val reason: Reason? = null,
    val teacherAppLink: String? = null,
) {

    enum class Reason {
        Authenticate, ChangePassword, FirstLogin, TeacherHaveNotAccess, HaveNotActiveMemberships
    }
}