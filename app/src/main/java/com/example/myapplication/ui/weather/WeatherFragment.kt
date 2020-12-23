package com.example.myapplication.ui.weather

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.example.myapplication.R
import com.example.myapplication.pojo.city.CityItem
import kotlinx.android.synthetic.main.fragment_weather.*

class WeatherFragment : Fragment() {

    // 初始化viewModel
    lateinit var viewModel: WeatherViewModel

    companion object {
        fun newInstance() = WeatherFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_weather, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(WeatherViewModel::class.java)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(WeatherViewModel::class.java)

        viewModel.cities.observe(viewLifecycleOwner, Observer {
            val cities = it
//            val adapter = ArrayAdapter<CityItem>(this,android.R.layout.simple_list_item_1, cities)
            val adapter = activity?.let { it1 -> ArrayAdapter<CityItem>(it1,android.R.layout.simple_list_item_1, cities) }
            weather_listView.adapter = adapter

            weather_listView.setOnItemClickListener { _, _, position, _ ->
                val cityCode = cities[position].city_code
                // 这里必须用activity, 因为此刻处于fragment布局中
                val intent = Intent(activity, WeatherInfoActivity::class.java)
                intent.putExtra("city_code", cityCode)
                startActivity(intent)
            }
        })
    }
}