package ru.vladik.opendiary.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import ru.vladik.opendiary.databinding.ActivityLoginBinding
import ru.vladik.opendiary.dialogs.PersonPickerDialog
import ru.vladik.opendiary.dnevnikapi.DiaryApi
import ru.vladik.opendiary.dnevnikapi.models.v7.ContextPersonV7
import ru.vladik.opendiary.example.activities.MainActivityExample
import ru.vladik.opendiary.ext.startActivity
import ru.vladik.opendiary.viewmodels.DiaryGetViewModel
import ru.vladik.opendiary.viewmodels.errorhandling.Resource
import ru.vladik.opendiary.viewmodels.errorhandling.ResourceObserver
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**

# Активити для входа в приложение "Дневник.ру".

### Предоставляет пользователю возможность ввести логин и пароль, после чего осуществляет авторизацию.

* @property mBinding экземпляр класса ActivityLoginBinding, содержащий ссылки на компоненты пользовательского интерфейса.
* @property mDiaryGet экземпляр класса DiaryGetViewModel, отвечающий за работу с API и хранение данных.
 */

class LoginActivity : AppCompatActivity() {
    private lateinit var mBinding: ActivityLoginBinding
    private lateinit var mDiaryGet: DiaryGetViewModel

    /**
        ### Создает активити и настраивает компоненты пользовательского интерфейса.

        Инициализирует mDiaryGet и устанавливает слушателя на получение данных из API.

        При успешной авторизации сохраняет информацию о пользователе и запускает главную активити.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        mDiaryGet = ViewModelProvider(this)[DiaryGetViewModel::class.java]
        setupViews()
        mDiaryGet.diaryApi.observe(this, object : ResourceObserver<DiaryApi>() {
            override fun onReady(v: DiaryApi) {
                startActivity(MainActivity::class.java, true)
                finish()
            }

            override fun onError(e: Resource.ResourceError?) {
                mBinding.passwordEditText.error = e?.message
            }
        })
    }

    /**
    ### Настраивает слушателей для компонентов пользовательского интерфейса.
     */

    private fun setupViews() {
        mBinding.button.setOnClickListener {
            onLoginButtonClick(it)
        }
        mBinding.buttonTest.setOnClickListener {
            startTest()
        }
    }

    /**

    ### Обрабатывает нажатие на кнопку входа в приложение.

        Получает логин и пароль, передает их в [mDiaryGet] для осуществления авторизации.

        * @param view компонент [View], на который был совершен клик.
     */

    private fun onLoginButtonClick(view: View) {
        val login = mBinding.loginEditText.text.toString()
        val password = mBinding.passwordEditText.text.toString()

        mDiaryGet.getDiaryAndSaveUser(this, login, password, PersonChooserImpl())
    }

    /**
    ## Реализация интерфейса [DiaryApi.PersonChooser].
    ###    Показывает диалог выбора пользователя, если их количество больше одного.
        Если количество пользователей равно единице, то сразу выбирается этот пользователь.
     */

    private inner class PersonChooserImpl : DiaryApi.PersonChooser {
        override suspend fun choose(persons: List<ContextPersonV7>): ContextPersonV7
        = suspendCoroutine { continuation ->
            if (persons.size == 1) {
                continuation.resume(persons[0])
                return@suspendCoroutine
            }
            val dialog = PersonPickerDialog(this@LoginActivity, persons.toMutableList()) { person ->
                continuation.resume(person)
            }
            dialog.show()
        }
    }

    /**
     * ### Запускает тестовую [MainActivityExample]
     */
    private fun startTest() {
        startActivity(MainActivityExample::class.java, true)
    }
}