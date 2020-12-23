package com.example.myapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myapplication.R
import com.example.myapplication.pojo.CityItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread

// 应用上下文, 需要AndroidViewModel
class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    // 内部使用
    private val _cities:MutableLiveData<List<CityItem>> = MutableLiveData()

    //外部调用
    val cities = _cities

    init {
        thread {
            val citycode = readFileFromRaw(R.raw.citycode)
            val gson = Gson()
            val CityType = object : TypeToken<List<CityItem>>() {}.type
            var cts: List<CityItem> = gson.fromJson(citycode, CityType)
            cts = cts.filter { it.city_code != "" }
            // 子线程, 使用postValue
            _cities.postValue(cts)
        }
    }

    //读raw目录下的资源文件（该文件的内容不经过编译）
    fun readFileFromRaw(rawName: Int): String? {
        try {
            val inputReader = InputStreamReader(getApplication<Application>().resources.openRawResource(rawName))
            val bufReader = BufferedReader(inputReader)
            var line: String? = ""
            var result: String? = ""
            while (bufReader.readLine().also({ line = it }) != null) {
                result += line
            }
            return result
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }
}