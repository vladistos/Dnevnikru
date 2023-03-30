package ru.vladik.opendiary.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.vladik.opendiary.R
import ru.vladik.opendiary.dnevnikapi.DiaryApi
import ru.vladik.opendiary.ext.startActivity
import ru.vladik.opendiary.viewmodels.DiaryGetViewModel
import ru.vladik.opendiary.viewmodels.errorhandling.Resource
import ru.vladik.opendiary.viewmodels.errorhandling.ResourceObserver


/**

# Активность запуска приложения.

* ### Этот класс наследуется от класса [AppCompatActivity] и отвечает за запуск приложения. В методе [onCreate] происходит инициализация [DiaryGetViewModel], установка контента на экране и настройка наблюдателей за изменением состояния дневника пользователя.

* @constructor Создает новый экземпляр класса [StarterActivity].
 */

class StarterActivity : AppCompatActivity() {
    private lateinit var viewModel: DiaryGetViewModel

    /**
     *
     * ### Вызывается при создании активности.
     * В этом методе происходит инициализация [ViewModel], установка контента на экране и настройка
     * наблюдателей за изменением состояния дневника пользователя.
     * @param savedInstanceState сохраненное состояние экрана.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[DiaryGetViewModel::class.java]
        setContentView(R.layout.launcher_layout)

        viewModel.diaryApi.observe(this, object : ResourceObserver<DiaryApi>() {

            override fun onReady(v: DiaryApi) {
                startActivity(MainActivity::class.java, true)
                viewModel.diaryApi.removeObserver(this)
            }

            override fun onError(e: Resource.ResourceError?) {
                startActivity(LoginActivity::class.java, true)
                viewModel.diaryApi.removeObserver(this)
            }

        })

        viewModel.getDiaryForLastUser(this)
    }
}