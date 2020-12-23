package com.example.myapplication.ui.weather

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.myapplication.R
import com.example.myapplication.pojo.weather.Forecast
import com.example.myapplication.pojo.weather.Weather
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_weather_info.*

class WeatherInfoActivity : AppCompatActivity() {
    val baseURL = "http://t.weather.itboy.net/api/weather/city/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_info)

        val cityCode = intent.getStringExtra("city_code")

        val queue = Volley.newRequestQueue(this)

        //主线程中响应，不需要特意放进主进程
        val stringRequest = StringRequest(baseURL + cityCode,{
            val gson = Gson()
            val WeatherType = object : TypeToken<Weather>(){}.type
            val weather: Weather = gson.fromJson<Weather>(it, WeatherType)
            weather_info_city.text = weather.cityInfo.city
            weather_info_parent.text = weather.cityInfo.parent
            weather_info_updateTime.text = weather.cityInfo.updateTime
            weather_info_shidu.text = "湿度：" + weather.data.shidu
            when (weather.data.quality) {
                "优" -> weather_info_quality.setTextColor(Color.GREEN)
                "良" -> weather_info_quality.setTextColor(Color.BLUE)
                else -> weather_info_quality.setTextColor(Color.RED)
            }
            weather_info_quality.text = "质量：" + weather.data.quality
            weather_info_wendu.text = "温度：" + weather.data.wendu + "℃"
            val currentDay = weather.data.forecast.first()
            when (currentDay.type) {
                "晴" -> weather_info_imageView.setImageResource(R.drawable.sun)
                "多云" -> weather_info_imageView.setImageResource(R.drawable.mcloud)
                "阴" -> weather_info_imageView.setImageResource(R.drawable.cloud)
                "阵雨" -> weather_info_imageView.setImageResource(R.drawable.rain)
                else -> weather_info_imageView.setImageResource(R.drawable.thunder)
            }

            val adapter = ArrayAdapter<Forecast>(this,android.R.layout.simple_list_item_1,weather.data.forecast)
            weather_info_listView.adapter = adapter

            Toast.makeText(this,"${weather.data.ganmao}", Toast.LENGTH_SHORT).show()
        },{
            Toast.makeText(this,"$it", Toast.LENGTH_SHORT).show()
        })
        queue.add(stringRequest)
    }
}