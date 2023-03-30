package ru.vladik.dnevnikru.dnevnikapi

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.*
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
Интерфейс для вызова API Дневник.ру
 */
interface DnevnikApiCaller {


    @Headers("Accept: application/json")
    @POST("/mobile/v7.0/authorizations/bycredentials")
    fun login(@Body authInfo: AuthInfo): Call<LoginResponse>

    @GET("/mobile/v7.0/users/{userId}/context")
    fun getContext(@Path("userId") userId: Long) : Call<UserContextV7>

    @GET("/v2/users/me/context")
    fun getContext() : Call<UserContextV2>

    @GET("/mobile/v7.0/persons/{personId}/groups/{groupId}/important")
    fun getFeed(@Path("personId") personId: Long, @Path("groupId") groupId: Long) : Call<UserFeedResponse>

    @GET("/v2/persons/{personId}/group/{groupId}/recentmarks")
    fun getRecentMarks(@Path("personId") personId: Long, @Path("groupId") groupId: Long) : Call<RecentMarksResp>

    @GET("/mobile/v7.0/persons/{personId}/schools/{schoolId}/groups/{groupId}/diary")
    fun getUserDayBook(
        @Path("personId") personId: Long,
        @Path("schoolId") schoolId: Long,
        @Path("groupId") groupId: Long,
        @Query("id") id: String?,
        @Query("loadType") loadType: String?
    ): Call<ScheduleV7>

    @GET("v2/persons/{personId}/edu-groups/{groupId}/marks/{dateStart}/{dateEnd}")
    fun getMarksForPeriod(@Path("personId") personId: Long, @Path("groupId") groupId: Long,
                          @Path("dateStart") dateStart: String, @Path("dateEnd") dateEnd: String)
    : Call<List<MarkV2>>

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