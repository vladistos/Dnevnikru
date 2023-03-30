package ru.vladik.dnevnikru.activities

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.vladik.dnevnikru.R
import ru.vladik.dnevnikru.databinding.ActivityMainBinding


/**

Главная активность приложения.

* Этот класс наследуется от класса [AppCompatActivity] и отвечает за создание и инициализацию
* главного экрана приложения. В методе [onCreate] происходит заполнение экрана и настройка
* навигации с помощью [BottomNavigationView] и [NavHostFragment].

* @constructor Создает новый экземпляр класса [MainActivity].
 */
class MainActivity : AppCompatActivity() {

    /**

    Вызывается при создании активности.

    В этом методе происходит заполнение экрана, настройка навигации и прочие необходимые
    действия при запуске активности.

    * @param savedInstanceState сохраненное состояние экрана.
     */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ActivityMainBinding.inflate(LayoutInflater.from(this)).apply {
            setContentView(root)
            val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            bottomNavigationView.setupWithNavController(navHost.navController)
        }

    }

}