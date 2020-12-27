package com.example.myapplication

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.ui.game.GameFragment
import com.example.myapplication.ui.music.MusicFragment
import com.example.myapplication.ui.time.TimeFragment
import com.example.myapplication.ui.weather.WeatherFragment
import kotlinx.android.synthetic.main.fragment_home.*


class MainActivity : AppCompatActivity(){

    private var fg2: TimeFragment? = null
    private var fg3: GameFragment? = null
    private var fg4: MusicFragment? = null
    private var fg5: WeatherFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(setOf(
                R.id.navigation_home, R.id.navigation_time, R.id.navigation_game, R.id.navigation_music, R.id.navigation_weather))
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }

    //显示第二个
    fun initTime() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (fg2 == null){
            fg2 = TimeFragment()
            transaction.add(R.id.navigation_time, fg2!!)
        }
        transaction.commit()
    }

    //显示第三个
    fun initGame() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (fg3 == null){
            fg3 = GameFragment()
            transaction.replace(R.id.navigation_game, fg3!!)
        }
        transaction.commit()
    }

    //显示第四个
    fun initMusic() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (fg4 == null){
            fg4 = MusicFragment()
            transaction.replace(R.id.navigation_music, fg4!!)
        }
        transaction.commit()
    }

    //显示第五个
    fun initWeather() {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        if (fg5 == null){
            fg5 = WeatherFragment()
            transaction.replace(R.id.navigation_weather, fg5!!)
        }
        transaction.commit()
    }
}