package ru.vladik.opendiary.dnevnikapi

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import kotlinx.coroutines.*
import ru.vladik.opendiary.AppConstants
import ru.vladik.opendiary.dnevnikapi.db.DiaryDatabase
import ru.vladik.opendiary.dnevnikapi.db.models.ApiUserImpl
import ru.vladik.opendiary.dnevnikapi.exceptions.LastUserIsEmptyException

/**
 * ## Вспомогательный объект для работы с данными в приложении.
 * - Предоставляет методы для работы с базой данных и [SharedPreferences].
 * - Реализован как синглтон, реализующий интерфейс [CoroutineScope].
 */
object DataHelper : CoroutineScope by MainScope() {
    private const val DEFAULT_LONG = -1L

    /**

    * ### Получает экземпляр [базы данных][DiaryDatabase].
    * @param context контекст приложения.
    * @return [DiaryDatabase] экземпляр базы данных.
     */
    private fun getDB(context: Context) =
        Room.databaseBuilder(context, DiaryDatabase::class.java, AppConstants.DIARY_DB_NAME).build()

    /**
    * ### Получает экземпляр [SharedPreferences].
    * @param context контекст приложения.
    * @return экземпляр [SharedPreferences].
     */
    private fun getSP(context: Context) =
        context.getSharedPreferences(AppConstants.SP_NAME, Context.MODE_PRIVATE)

    /**
    * ### Редактирует [SharedPreferences].
    * @param context контекст приложения.
    * @param withSP функция, редактирующая [SharedPreferences].
     */
    private fun editSP(context: Context, withSP: (sp: SharedPreferences.Editor) -> Unit) {
        val spEditor = getSP(context).edit()
        withSP(spEditor)
        spEditor.apply()
    }

    /**
     * ### Выбрасывает [исключение][LastUserIsEmptyException]. Все.
     * @throws LastUserIsEmptyException исключение, выбрасываемое при отсутствии сохраненного пользователя.
     */
    private fun throwNoUser() {
        throw LastUserIsEmptyException()
    }

    /**

    * ### Получает последнего сохраненного пользователя.
    * @param context контекст приложения.
    * @return экземпляр класса ApiUserImpl, представляющий последнего сохраненного пользователя.
    * @throws LastUserIsEmptyException исключение, выбрасываемое при отсутствии последнего пользователя.
     */
    suspend fun getLastUser(context: Context): ApiUserImpl = coroutineScope {
        val id = getSP(context).getLong(AppConstants.SP_LAST_USER_ID, DEFAULT_LONG)
        if (id == DEFAULT_LONG) {
            throwNoUser()
        }
        val user = getDB(context).userDao().getById(id)
        if (user == null) throwNoUser()
        return@coroutineScope user!!
    }

    /**
    * ### Сохраняет пользователя в [базе данных][DiaryDatabase] и [SharedPreferences].
    * @param context контекст приложения.
    * @param user экземпляр класса ApiUserImpl, который необходимо сохранить.
    * @throws NullPointerException исключение выбрасывается, если [ApiUserImpl.primaryKey] == null (никогда).
     */
    suspend fun saveLastUser(context: Context, user: ApiUserImpl) =
        withContext(Dispatchers.Default) {
            val id = user.primaryKey
            getDB(context).userDao().insert(user)
            editSP(context) {
                it.putLong(AppConstants.SP_LAST_USER_ID, id ?: throw NullPointerException())
            }
        }

}