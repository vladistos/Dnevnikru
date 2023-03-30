package ru.vladik.opendiary

import java.text.SimpleDateFormat
import java.util.*

object ApiConstants {
    const val DNEVNIK_API_URL = "https://api.dnevnik.ru"
    const val SCOPE = "Schools,Relatives,EduGroups,Lessons,marks,EduWorks,Avatar"
    const val API_KEY = "1d7bd105-4cd1-4f6c-9ecc-394e400b53bd"
    const val API_SECRET = "5dcb5237-b5d3-406b-8fee-4441c3a66c99"

}

object AppConstants {
    const val SP_LAST_USER_ID = "last_user_id"
    const val SP_NAME = "ru.vladik.sharedPreferences"
    const val USERS_DB_NAME = "users"
    const val DIARY_DB_NAME = "diary_database"

    val APP_DATE_FORMAT_FULL = SimpleDateFormat("yyyy.MM.dd HH:mm", Locale.ROOT)
    val DIARY_WEEK_START_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.ROOT)
    val API_V2_DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.ROOT)
    val HOUR_MINUTE_FORMAT = SimpleDateFormat("HH:mm", Locale.ROOT)
}