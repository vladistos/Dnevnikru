package ru.vladik.dnevnikru.dnevnikapi

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*
import ru.vladik.dnevnikru.AppConstants.API_V2_DATE_FORMAT
import ru.vladik.dnevnikru.AppConstants.DIARY_WEEK_START_DATE_FORMAT
import ru.vladik.dnevnikru.dnevnikapi.models.AuthInfo
import ru.vladik.dnevnikru.dnevnikapi.models.LoginResponse
import ru.vladik.dnevnikru.dnevnikapi.models.v2.LessonV2
import ru.vladik.dnevnikru.dnevnikapi.models.v2.MarkV2
import ru.vladik.dnevnikru.dnevnikapi.models.v2.RecentMarksResp
import ru.vladik.dnevnikru.dnevnikapi.models.v2.UserContextV2
import ru.vladik.dnevnikru.dnevnikapi.models.v7.ScheduleV7
import ru.vladik.dnevnikru.dnevnikapi.models.v7.UserContextV7
import ru.vladik.dnevnikru.dnevnikapi.models.v7.UserFeedResponse

/**
#Интерфейс для вызова API Дневник.ру
 */
interface DnevnikApiCaller {


    /**
     * ###Выполняет авторизацию пользователя на сервере Дневника.ru.
     *
     * @param authInfo информация для авторизации пользователя.
     * @return экземпляр класса [Call] с ответом на запрос [LoginResponse].
     */
    @Headers("Accept: application/json")
    @POST("/mobile/v7.0/authorizations/bycredentials")
    fun login(@Body authInfo: AuthInfo): Call<LoginResponse>

    /**
     * ###Получает контекст пользователя.
     *
     * @param userId идентификатор пользователя.
     * @return экземпляр класса [Call] с ответом на запрос [UserContextV7].
     */
    @GET("/mobile/v7.0/users/{userId}/context")
    fun getSelfContext(@Path("userId") userId: Long) : Call<UserContextV7>

    /**
     * ###Получает контекст текущего пользователя (пользователя, токен аккаунта которого используется).
     *
     * @return экземпляр класса [Call] с ответом на запрос [UserContextV2].
     */
    @GET("/v2/users/me/context")
    fun getSelfContext() : Call<UserContextV2>

    /**
     * ###Получает данные ленты новостей пользователя.
     *
     * @param personId идентификатор персоны пользователя.
     * @param groupId идентификатор учебной группы.
     * @return экземпляр класса [Call] с ответом на запрос [UserFeedResponse].
     */
    @GET("/mobile/v7.0/persons/{personId}/groups/{groupId}/important")
    fun getFeed(@Path("personId") personId: Long, @Path("groupId") groupId: Long) : Call<UserFeedResponse>

    /**
     * ###Получает последние оценки пользователя.
     *
     * @param personId идентификатор персоны пользователя.
     * @param groupId идентификатор учебной группы.
     * @return экземпляр класса [Call] с ответом на запрос [RecentMarksResp].
     */
    @GET("/v2/persons/{personId}/group/{groupId}/recentmarks")
    fun getRecentMarks(@Path("personId") personId: Long, @Path("groupId") groupId: Long) : Call<RecentMarksResp>

    /**
     * ###Получает расписание пользователя.
     *
     * @param personId идентификатор персоны пользователя.
     * @param schoolId идентификатор школы.
     * @param groupId идентификатор учебной группы.
     * @param id идентификатор учебной недели (дата понедельника в формате [DIARY_WEEK_START_DATE_FORMAT]).
     * @param loadType тип загрузки расписания:
     * - [ScheduleLoadType.FUTURE] возвращает следующую неделю
     * - [ScheduleLoadType.PAST] возвращает прошлую неделю.
     * @return экземпляр класса [Call] с ответом на запрос [ScheduleV7].
     */

    @GET("/mobile/v7.0/persons/{personId}/schools/{schoolId}/groups/{groupId}/diary")
    fun getUserDayBook(
        @Path("personId") personId: Long,
        @Path("schoolId") schoolId: Long,
        @Path("groupId") groupId: Long,
        @Query("id") id: String?,
        @Query("loadType") loadType: String?
    ): Call<ScheduleV7>

    /**
     * ###Получает оценки персоны за указанный период
     *
     * @param personId идентификатор персоны пользователя.
     * @param groupId идентификатор учебной группы.
     * @param dateStart дата начала периода для получения оценок в формате [API_V2_DATE_FORMAT]
     * @param dateEnd дата окончания периода для получения оценок в формате [API_V2_DATE_FORMAT]
     *
     * @return [Call], возвращающий [List] из [MarkV2].
     */

    @GET("v2/persons/{personId}/edu-groups/{groupId}/marks/{dateStart}/{dateEnd}")
    fun getMarksForPeriod(@Path("personId") personId: Long, @Path("groupId") groupId: Long,
                          @Path("dateStart") dateStart: String, @Path("dateEnd") dateEnd: String)
    : Call<List<MarkV2>>

    /**
     * ###Получает уроки учебной группы за период
     *
     * @param groupId идентификатор учебной группы.
     * @param dateStart дата начала периода для получения оценок в формате [API_V2_DATE_FORMAT]
     * @param dateEnd дата окончания периода для получения оценок в формате [API_V2_DATE_FORMAT]
     *
     * @return [Call], возвращающий [List] из [LessonV2]
     */
    @GET("v2/edu-groups/{groupId}/lessons/{dateStart}/{dateEnd}")
    fun getLessonsForPeriod(@Path("groupId") groupId: Long,
                            @Path("dateStart") dateStart: String, @Path("dateEnd") dateEnd: String)
    : Call<List<LessonV2>>


    enum class ScheduleLoadType(val text: String) {
        @SerializedName("Future")
        FUTURE("Future"),

        @SerializedName("Past")
        PAST("Past"),

        @SerializedName("Undefined")
        UNDEFINED("Undefined")
    }
}