package ru.vladik.dnevnikru.dnevnikapi

import kotlinx.coroutines.coroutineScope
import ru.vladik.dnevnikru.AppConstants
import ru.vladik.dnevnikru.dnevnikapi.db.models.ApiUserImpl
import ru.vladik.dnevnikru.dnevnikapi.exceptions.NoSuchUserException
import ru.vladik.dnevnikru.dnevnikapi.exceptions.NullResponseException
import ru.vladik.dnevnikru.dnevnikapi.exceptions.ResError
import ru.vladik.dnevnikru.dnevnikapi.exceptions.TokenExpiredException
import ru.vladik.dnevnikru.dnevnikapi.models.v7.ContextPersonV7
import ru.vladik.dnevnikru.ext.http.enqueue
import java.util.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import retrofit2.Response as RetrofitResponse

class DiaryApi private constructor(var user: ApiUserImpl) {

    private var method: DnevnikApiCaller = DnevnikApiProvider.provide(user.token)

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

    companion object {
        suspend fun construct(login: String, password: String)
        = coroutineScope {
            val token = DnevnikApiProvider.provideToken(login, password)

            val user = ApiUserImpl(token = token, login = login, password = password)

            return@coroutineScope DiaryApi(user)

        }

        suspend fun forUser(apiUser: ApiUserImpl) = coroutineScope {
            return@coroutineScope DiaryApi(apiUser)
        }
    }

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

    suspend fun getUserContext(uId: Long) = suspendCoroutine {
            continuation ->
        method.getContext(uId).enqueue {
            continuation.process(it)
        }
    }

    suspend fun getRecentMarks(pId: Long, gId: Long) = suspendCoroutine { continuation ->
        method.getRecentMarks(pId, gId).enqueue {
            continuation.process(it)
        }
    }

    suspend fun getMarksForPeriod(personId: Long, groupId: Long, dateStart: Date, dateEnd: Date) =
        suspendCoroutine { continuation ->
            val start = AppConstants.API_V2_DATE_FORMAT.format(dateStart)
            val end = AppConstants.API_V2_DATE_FORMAT.format(dateEnd)
            method.getMarksForPeriod(personId, groupId, start, end).enqueue { continuation.process(it) }
        }

    suspend fun getLessonsForPeriod(groupId: Long, dateStart: Date, dateEnd: Date) =
        suspendCoroutine { continuation ->
            val start = AppConstants.API_V2_DATE_FORMAT.format(dateStart)
            val end = AppConstants.API_V2_DATE_FORMAT.format(dateEnd)
            method.getLessonsForPeriod(groupId, start, end).enqueue { continuation.process(it) }
        }

    suspend fun getSelfContext() = suspendCoroutine {
            continuation ->
        method.getContext().enqueue {
            continuation.process(it)
        }
    }

    suspend fun getUserFeed(pId: Long, gId: Long) = suspendCoroutine { continuation ->
        method.getFeed(pId, gId).enqueue {
            continuation.process(it)
        }
    }

    suspend fun getUserDayBook(personId: Long, schoolId: Long, groupId: Long, id: String?,
                               loadType: DnevnikApiCaller.ScheduleLoadType?) = suspendCoroutine { continuation ->
        method.getUserDayBook(personId, schoolId, groupId, id, loadType?.text).enqueue {
            continuation.process(it)
        }
    }

    interface PersonChooser {
        suspend fun choose(persons: List<ContextPersonV7>): ContextPersonV7
    }

    class PersonChooserById(val id: Long) : PersonChooser {
        override suspend fun choose(persons: List<ContextPersonV7>): ContextPersonV7 {
            val user = persons.find { person -> person.personId == id }
            return user ?: throw NoSuchUserException()
        }
    }
}