package com.example.myapplication.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import com.example.myapplication.ui.game.GameFragment
import com.example.myapplication.ui.music.MusicFragment
import com.example.myapplication.ui.time.TimeFragment
import com.example.myapplication.ui.weather.WeatherFragment
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_time.*

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    private var fg2: TimeFragment? = null
    private var fg3: GameFragment? = null
    private var fg4: MusicFragment? = null
    private var fg5: WeatherFragment? = null

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
//        val textView: TextView = root.findViewById(R.id.text_home)
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })

        var mainActivity:MainActivity? = null

        val time:Button = root.findViewById(R.id.btn_time)
        time.setOnClickListener {
//            Navigation.createNavigateOnClickListener(R.id.navigation_time, null)
            NavHostFragment.findNavController(this)
                .popBackStack(
                    R.id.navigation_time,false
                )
//            mainActivity?.initTime()
            Log.d("info","time")
        }

        val game:Button = root.findViewById(R.id.btn_game)
        game.setOnClickListener {
            mainActivity?.initGame()
            Log.d("info","game")
        }

        val music:Button = root.findViewById(R.id.btn_music)
        music.setOnClickListener {
            mainActivity?.initMusic()
            Log.d("info","music")
        }

        val weather:Button = root.findViewById(R.id.btn_weather)
        weather.setOnClickListener {
            mainActivity?.initWeather()
            Log.d("info","weather")
        }

        return root
    }
}