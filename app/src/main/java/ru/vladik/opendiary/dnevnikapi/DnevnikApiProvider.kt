package ru.vladik.opendiary.dnevnikapi

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrl
import org.jsoup.Jsoup
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import ru.vladik.opendiary.ApiConstants
import ru.vladik.opendiary.dnevnikapi.exceptions.LoginException
import ru.vladik.opendiary.dnevnikapi.models.v7.FeedInfoPrototype
import ru.vladik.opendiary.dnevnikapi.models.v7.deserializers.FeedInfoDeserializer
import ru.vladik.opendiary.ext.http.addParam
import ru.vladik.opendiary.ext.http.enqueue
import java.net.CookieManager
import java.net.CookiePolicy
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

/**
 * # Провайдер API для Dnevnik.ru. Предоставляет методы для получения токена, создания клиента для вызова API и другие вспомогательные функции.
 */

object DnevnikApiProvider {

    /**

    * [Gson] объект, используемый для десериализации ответов от сервера.
     */
    private val gson by lazy {
        val feedInfoDeserializer = FeedInfoDeserializer()
        GsonBuilder()
            .registerTypeAdapter(FeedInfoPrototype::class.java, feedInfoDeserializer)
            .create()
    }

    /**
    * [Retrofit.Builder], используемый для создания клиента для вызова API.
     */
    private val retrofitBuilder by lazy {
        Retrofit.Builder().apply {
            baseUrl(ApiConstants.DNEVNIK_API_URL)
            addConverterFactory(GsonConverterFactory.create(gson))
        }
    }


    /**
     * ### Принимает [token] в качестве параметра и добавляет его в заголовок каждого запроса.
     *
     * @param token Токен, полученный при авторизации.
     * @return [OkHttpClient], в с [Interceptor.Chain], которая добавляет заголовок с токеном.
     */
    private fun getClient(token: String): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor {
                val request = it.request().newBuilder().apply {
                    addHeader("Access-Token", token)
                }.build()
                return@addInterceptor it.proceed(request)
            }.build()
    }


    /**

    ### Возвращает клиента для вызова API с указанным токеном доступа.
    * @param token токен доступа, полученный после аутентификации.
    * @return [DnevnikApiCaller] клиент для вызова API.
     */
    fun provide(token: String) : DnevnikApiCaller {
        return retrofitBuilder.client(getClient(token)).build().create()
    }

    /**

    ### Возвращает токен доступа для указанного логина и пароля.

    * @param login логин пользователя.
    * @param password пароль пользователя.
    * @return [String] токен доступа.
    * @throws LoginException если аутентификация не удалась.
     */
    suspend fun provideToken(login: String, password: String): String = suspendCoroutine {
        val cookieManager = CookieManager()
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL)
        val returnUrl = "https://login.dnevnik.ru/oauth2/".toHttpUrl()
            .addParam("response_type", "token")
            .addParam("client_id", "bb97b3e445a340b9b9cab4b9ea0dbd6f")
            .addParam("scope", ApiConstants.SCOPE)

        val url = "https://login.dnevnik.ru/login/".toHttpUrl()
            .addParam("ReturnUrl", returnUrl.toString())
            .addParam("login", login)
            .addParam("password", password)

        val request = Request.Builder()
            .url(url)
            .post(FormBody.Builder().build())
            .build()

        OkHttpClient.Builder()
            .cookieJar(JavaNetCookieJar(cookieManager))
            .build()
            .newCall(request)
            .enqueue { resp ->
                val responseURL: String = resp.request.url.toString()
                if (responseURL.contains("#access_token=")) {
                    val token = responseURL.substring(
                        responseURL.lastIndexOf("#access_token="),
                        responseURL.indexOf("&state=")
                    ).replace("#access_token=", "")
                    resp.close()
                    it.resume(token)
                    return@enqueue
                }
                val errStr = getError(resp.body?.string())
                it.resumeWithException(LoginException(errStr))
            }
    }

    /**

    ### Парсит html страницы с ошибкой и возвращает сообщение об ошибке при его наличии
     * @param html строка ответа с сайта
     * [Дневник ру](https://login.dnevnik.ru/login/)
     * @return [String] текст ошибки
     */
    private fun getError(html: String?): String {
        if (html == null) return ""
        val jsoup = Jsoup.parse(html)
        val elementVal = jsoup.getElementsByAttributeValue("class", "message")
        val messageDiv = if (elementVal.toArray().isNotEmpty()) elementVal[0] else null
        return messageDiv?.text() ?: ""
    }

}