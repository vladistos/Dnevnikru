package ru.vladik.opendiary.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ru.vladik.opendiary.dnevnikapi.DataHelper
import ru.vladik.opendiary.dnevnikapi.DiaryApi
import ru.vladik.opendiary.dnevnikapi.db.models.ApiUserImpl
import ru.vladik.opendiary.dnevnikapi.exceptions.LastUserIsEmptyException
import ru.vladik.opendiary.dnevnikapi.exceptions.TokenExpiredException
import ru.vladik.opendiary.viewmodels.errorhandling.ResourceLiveData


/**
    # [ViewModel] для получения [DiaryApi], который в дальнейшем используется для запросов к API
 */


open class DiaryGetViewModel : ViewModel() {

    /**
        [ResourceLiveData] для отслеживания состояния загрузки [DiaryApi]
     */

    val diaryApi = ResourceLiveData<DiaryApi>()

    /**

        Асинхронный метод для получения [DiaryApi]
        * @param login логин пользователя
        * @param password пароль пользователя
        * @param personChooser реализация выбора персоны для использования API
     */

    fun getDiary(login: String, password: String,
                 personChooser: DiaryApi.PersonChooser) = viewModelScope.launchAsync {
        try {
            val api = DiaryApi.construct(login, password)
            api.init(personChooser)
            diaryApi.postResValue(api)
            DiaryApiSingleton.api = api
        } catch (e: Exception) {
            diaryApi.postResError(e.message.toString(), e)
        }
    }

    /**
        Асинхронный метод для сохранения последнего пользователя, который использовал [DiaryApi]
        * @param context контекст, необходимый для доступа к БД
        * @param user пользователь, чьи данные нужно сохранить
     */

    fun saveLastUser(context: Context, user: ApiUserImpl) = viewModelScope.launchAsync {
        DataHelper.saveLastUser(context, user)
    }


    fun getDiaryAndSaveUser(context: Context, login: String, password: String,
                             personChooser: DiaryApi.PersonChooser) = viewModelScope.launchAsync {
        try {
            val api = DiaryApi.construct(login, password)
            api.init(personChooser)
            diaryApi.postResValue(api)
            DiaryApiSingleton.api = api
            saveLastUser(context, api.user)
        } catch (e: Exception) {
            diaryApi.postResError(e.message.toString(), e)
        }
    }

    /**
        Асинхронный метод для получения [DiaryApi] для последнего пользователя, который использовал API
        * @param context контекст, необходимый для доступа к БД
     */

    fun getDiaryForLastUser(context: Context) = viewModelScope.launchAsync {
        try {
            var api = DiaryApiSingleton.api
            if (api != null) {
                diaryApi.postResValue(DiaryApiSingleton.api!!)
                return@launchAsync
            }
            val lastUser = DataHelper.getLastUser(context)
            api = DiaryApi.forUser(lastUser)
            try {
                api.init(
                    DiaryApi.PersonChooserById(
                        lastUser.personId ?: throw LastUserIsEmptyException()
                    )
                )
            } catch (authErr: TokenExpiredException) {
                authErr.printStackTrace()
                getDiaryAndSaveUser(context, lastUser.login, lastUser.password,
                    DiaryApi.PersonChooserById(lastUser.personId ?: throw LastUserIsEmptyException()))
            }
            diaryApi.postResValue(api)
            DiaryApiSingleton.api = api
        } catch (e: Exception) {
            e.printStackTrace()
            diaryApi.postResError(e.message.toString(), e)
        }
    }

    /**
        Синглтон для хранения [DiaryApi]
     */

    object DiaryApiSingleton {
        var api: DiaryApi? = null
    }

}