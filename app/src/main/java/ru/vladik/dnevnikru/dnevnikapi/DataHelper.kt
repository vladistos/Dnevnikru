package ru.vladik.dnevnikru.dnevnikapi

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import kotlinx.coroutines.*
import ru.vladik.dnevnikru.AppConstants
import ru.vladik.dnevnikru.dnevnikapi.db.DiaryDatabase
import ru.vladik.dnevnikru.dnevnikapi.db.models.ApiUserImpl
import ru.vladik.dnevnikru.dnevnikapi.exceptions.LastUserIsEmptyException

object DataHelper : CoroutineScope by MainScope() {
    private const val DEFAULT_LONG = -1L

    private fun getDB(context: Context) =
        Room.databaseBuilder(context, DiaryDatabase::class.java, AppConstants.DIARY_DB_NAME).build()

    private fun getSP(context: Context) =
        context.getSharedPreferences(AppConstants.SP_NAME, Context.MODE_PRIVATE)

    private fun editSP(context: Context, withSP: (sp: SharedPreferences.Editor) -> Unit) {
        val spEditor = getSP(context).edit()
        withSP(spEditor)
        spEditor.apply()
    }

    private fun throwNoUser() {
        throw LastUserIsEmptyException()
    }

    suspend fun getLastUser(context: Context): ApiUserImpl = coroutineScope {
        val id = getSP(context).getLong(AppConstants.SP_LAST_USER_ID, DEFAULT_LONG)
        if (id == DEFAULT_LONG) {
            throwNoUser()
        }
        val user = getDB(context).userDao().getById(id)
        if (user == null) throwNoUser()
        return@coroutineScope user!!
    }

    suspend fun saveLastUser(context: Context, user: ApiUserImpl) =
        withContext(Dispatchers.Default) {
            val id = user.primaryKey
            getDB(context).userDao().insert(user)
            editSP(context) {
                it.putLong(AppConstants.SP_LAST_USER_ID, id ?: throw NullPointerException())
            }
        }

}