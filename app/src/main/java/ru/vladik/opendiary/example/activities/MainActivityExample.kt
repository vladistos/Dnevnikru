package ru.vladik.opendiary.example.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.vladik.opendiary.R
import ru.vladik.opendiary.activities.StarterActivity
import ru.vladik.opendiary.databinding.ActivityMainExampleBinding
import ru.vladik.opendiary.ext.startActivity

class MainActivityExample : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityMainExampleBinding.inflate(layoutInflater).run {
            setContentView(root)
            val navHost =
                supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            bottomNavigationView.setupWithNavController(navHost.navController)
            testModeText.setOnClickListener {
                startActivity(StarterActivity::class.java, true)
            }
        }
    }

}