package com.example.myapplication.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)

        val time:Button = root.findViewById(R.id.btn_time)
        time.setOnClickListener {
            NavHostFragment
                .findNavController(this)
                .navigate(R.id.action_homeFragment_to_timeFragment)
        }

        val game:Button = root.findViewById(R.id.btn_game)
        game.setOnClickListener {
            NavHostFragment
                .findNavController(this)
                .navigate(R.id.action_homeFragment_to_gameActivity)
        }

        val music:Button = root.findViewById(R.id.btn_music)
        music.setOnClickListener {
            NavHostFragment
                .findNavController(this)
                .navigate(R.id.action_homeFragment_to_musicActivity)
        }

        val weather:Button = root.findViewById(R.id.btn_weather)
        weather.setOnClickListener {
            NavHostFragment
                .findNavController(this)
                .navigate(R.id.action_homeFragment_to_weatherActivity)
        }

        return root
    }
}