package ru.vladik.opendiary.dnevnikapi

import kotlinx.coroutines.coroutineScope
import ru.vladik.opendiary.AppConstants
import ru.vladik.opendiary.dnevnikapi.DiaryApi.Companion
import ru.vladik.opendiary.dnevnikapi.db.models.ApiUserImpl
import ru.vladik.opendiary.dnevnikapi.exceptions.NoSuchUserException
import ru.vladik.opendiary.dnevnikapi.exceptions.NullResponseException
import ru.vladik.opendiary.dnevnikapi.exceptions.ResError
import ru.vladik.opendiary.dnevnikapi.exceptions.TokenExpiredException
import ru.vladik.opendiary.dnevnikapi.models.v7.ContextPersonV7
import ru.vladik.opendiary.ext.http.enqueue
import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import retrofit2.Response as RetrofitResponse

/**
 * # Данный класс предоставляет API для взаимодействия с сайтом dnevnik.ru.

    * **Поддерживается работа со следующими функциями**:

    *   - Авторизация и получение токена
    *   - Инициализация API для конкретного пользователя
    *   - Получение информации о пользователе (контекст)
    *   - Получение оценок за определенный период
    *   - Получение расписания уроков за определенный период
    *   - Получение ленты событий пользователя
    *   - Получение дневника пользователя
    * @constructor **Является приватным.** Создает объект [DiaryApi] для работы с сайтом [Дневник.ру](https://dnevnik.ru/)
 * Для получения экземпляра класса извне нужно воспользоваться [Companion]

    * @param user объект типа [ApiUserImpl], содержащий токен пользователя, логин и пароль
 */
class DiaryApi private constructor(var user: ApiUserImpl) {

    /**
        * Внутренняя переменная является экземпляром [DnevnikApiCaller] для вызова методов API.
        * Инициализируется при создании объекта DiaryApi.
     */

    private var mMethod: DnevnikApiCaller = DnevnikApiProvider.provide(user.token)


    /**
     * ### Внутренний метод для обработки ответов сервера. Обрабатывает некоторые из возможных ошибок.
     * Является расширением [Continuation]. Возвращает значение в случае, если ответ валиден, в ином случае
     * *бросает* соответствующую ошибку.
     * @param T тип данных ответа.
     * @param response [RetrofitResponse] с типом [T], который будет обработан.
     */
    private inline fun <reified T> Continuation<T>.process(response: RetrofitResponse<T>) {
        response.body()?.let { resp ->
            resume(resp)
            return
        }
        if (response.message() == "Unauthorized") {
            resumeWithException(TokenExpiredException())
            return
        }
        resumeWithException(NullResponseException("Null response while getting ${T::class.qualifiedName}"))
    }

    /**
    * ### Объект-компаньон для создания объекта [DiaryApi].
    */
    companion object {

        /**
            * ### Статический метод для создания объекта DiaryApi на основе логина и пароля пользователя.
            * Возвращает объект [DiaryApi] для работы с API сайта.

            * @param login логин пользователя
            * @param password пароль пользователя
            * @return объект [DiaryApi] для работы с API сайта.
        */

        suspend fun construct(login: String, password: String)
        = coroutineScope {
            val token = DnevnikApiProvider.provideToken(login, password)

            val user = ApiUserImpl(token = token, login = login, password = password)

            return@coroutineScope DiaryApi(user)

        }

        /**
        * ### Статический метод для создания объекта [DiaryApi] на основе объекта типа [ApiUserImpl].
        * Возвращает объект [DiaryApi] для работы с API сайта.
        * @param apiUser объект типа [ApiUserImpl], содержащий токен пользователя, логин и пароль.
        * @return объект [DiaryApi] для работы с API сайта.
         */

        suspend fun forUser(apiUser: ApiUserImpl) = coroutineScope {
            return@coroutineScope DiaryApi(apiUser)
        }
    }

    /**

    * ### Метод инициализирует объект [ApiUserImpl] для пользователя с помощью объекта [PersonChooser] и обновляет данные пользователя.
    *
    * @param personChooser объект, который используется для выбора контекстного пользователя

    * @throws ResError если произошла ошибка при получении контекста пользователя
    * @throws NullResponseException если контекст пользователя не был получен
    * @throws NoSuchUserException если контекстный пользователь не найден
     */
    suspend fun init(personChooser: PersonChooser) = coroutineScope {
        val selfContextV2 = getSelfContext()
        if (selfContextV2.errType != null) {
            throw ResError(selfContextV2.errType)
        }
        val id = selfContextV2.userId ?: throw NullResponseException()
        val context = getUserContext(id)
        val contextPersons = context.contextPersons ?: throw NullResponseException()
        val person = personChooser.choose(contextPersons)

        user.refreshData(person)
    }

    /**
     * ### Реализация версии одного из методов как корутины [DnevnikApiCaller]
     * @see DnevnikApiCaller.getSelfContext
     * @see <a href="https://kotlinlang.org/docs/coroutines-guide.html">Оффициальная документация по карутинам.</a>
     */
    suspend fun getUserContext(uId: Long) = suspendCoroutine {
            continuation ->
        mMethod.getSelfContext(uId).enqueue {
            continuation.process(it)
        }
    }

    /**
     * ### Реализация версии одного из методов как корутины [DnevnikApiCaller]
     * @see DnevnikApiCaller.getRecentMarks
     * @see <a href="https://kotlinlang.org/docs/coroutines-guide.html">Оффициальная документация по карутинам.</a>
     */
    suspend fun getRecentMarks(pId: Long, gId: Long) = suspendCoroutine { continuation ->
        mMethod.getRecentMarks(pId, gId).enqueue {
            continuation.process(it)
        }
    }

    /**
     * ### Реализация версии одного из методов как корутины [DnevnikApiCaller]
     *
     * **Вместо форматированной строки для начала и конца периода принимает экземпляр [Date]**
     * @see DnevnikApiCaller.getMarksForPeriod
     * @see <a href="https://kotlinlang.org/docs/coroutines-guide.html">Оффициальная документация по карутинам.</a>
     */
    suspend fun getMarksForPeriod(personId: Long, groupId: Long, dateStart: Date, dateEnd: Date) =
        suspendCoroutine { continuation ->
            val start = AppConstants.API_V2_DATE_FORMAT.format(dateStart)
            val end = AppConstants.API_V2_DATE_FORMAT.format(dateEnd)
            mMethod.getMarksForPeriod(personId, groupId, start, end).enqueue { continuation.process(it) }
        }

    /**
     * ### Реализация версии одного из методов как корутины [DnevnikApiCaller]
     *
     * **Вместо форматированной строки для начала и конца периода принимает экземпляр [Date]**
     * @see DnevnikApiCaller.getLessonsForPeriod
     * @see <a href="https://kotlinlang.org/docs/coroutines-guide.html">Оффициальная документация по карутинам.</a>
     */
    suspend fun getLessonsForPeriod(groupId: Long, dateStart: Date, dateEnd: Date) =
        suspendCoroutine { continuation ->
            val start = AppConstants.API_V2_DATE_FORMAT.format(dateStart)
            val end = AppConstants.API_V2_DATE_FORMAT.format(dateEnd)
            mMethod.getLessonsForPeriod(groupId, start, end).enqueue { continuation.process(it) }
        }

    /**
     * ### Реализация версии одного из методов как корутины [DnevnikApiCaller]
     * @see DnevnikApiCaller.getSelfContext
     * @see <a href="https://kotlinlang.org/docs/coroutines-guide.html">Оффициальная документация по карутинам.</a>
     */
    suspend fun getSelfContext() = suspendCoroutine {
            continuation ->
        mMethod.getSelfContext().enqueue {
            continuation.process(it)
        }
    }

    /**
     * ### Реализация версии одного из методов как корутины [DnevnikApiCaller]
     * @see DnevnikApiCaller.getFeed
     * @see <a href="https://kotlinlang.org/docs/coroutines-guide.html">Оффициальная документация по карутинам.</a>
     */
    suspend fun getUserFeed(pId: Long, gId: Long) = suspendCoroutine { continuation ->
        mMethod.getFeed(pId, gId).enqueue {
            continuation.process(it)
        }
    }

    /**
     * ### Реализация версии одного из методов как корутины [DnevnikApiCaller]
     * @see DnevnikApiCaller.getUserDayBook
     * @see <a href="https://kotlinlang.org/docs/coroutines-guide.html">Оффициальная документация по карутинам.</a>
     */
    suspend fun getUserDayBook(personId: Long, schoolId: Long, groupId: Long, id: String?,
                               loadType: DnevnikApiCaller.ScheduleLoadType?) = suspendCoroutine { continuation ->
        mMethod.getUserDayBook(personId, schoolId, groupId, id, loadType?.text).enqueue {
            continuation.process(it)
        }
    }

    /**
     * ## Интерфейс для выбора персоны в случае, если к пользователю привязано более одной [ContextPersonV7].
     */
    interface PersonChooser {
        /**
         * ### Функция для выбора персоны.
         * @param persons список персон из которых придется выбирать.
         * @return [ContextPersonV7] единственный выбранный экземпляр.
         */
        suspend fun choose(persons: List<ContextPersonV7>): ContextPersonV7
    }

    /**
     * ## Базовая реализация интерфейса [PersonChooser], выбирает персону по [id], если он известен заранее.
     *
     * @param id Идентификатор пользователя, которого нужно выбрать.
     * @see PersonChooser
     */
    class PersonChooserById(val id: Long) : PersonChooser {
        /**
         * @throws NoSuchUserException в случае, если в списке нет нужного пользователя.
         * @see PersonChooser.choose
         */
        override suspend fun choose(persons: List<ContextPersonV7>): ContextPersonV7 {
            val user = persons.find { person -> person.personId == id }
            return user ?: throw NoSuchUserException()
        }
    }
}