package com.example.myapplication.ui

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.pojo.CityItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_weather.*
import kotlin.concurrent.thread

class WeatherFragment : Fragment() {

    companion object {
        fun newInstance() = WeatherFragment()
    }

    private lateinit var viewModel: WeatherViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)

        viewModel.cities.observe(viewLifecycleOwner, Observer {
//            val adapter = ArrayAdapter<CityItem>(this,android.R.layout.simple_list_item_1, cities)
            val cities = it
            val adapter = activity?.let { it1 -> ArrayAdapter<CityItem>(it1,android.R.layout.simple_list_item_1, cities) }
            weather_listView.adapter = adapter

//        weather_listView.setOnItemClickListener { _, _, position, _ ->
//            val cityCode = cities[position].city_code
//            val intent = Intent(activity, Infomarion::class.java)
//            intent.putExtra("city_code", cityCode)
//            startActivity(intent)
//        }
        })
    }

}