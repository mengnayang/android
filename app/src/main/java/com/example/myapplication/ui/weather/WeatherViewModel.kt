package com.example.myapplication.ui.weather

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.R
import com.example.myapplication.pojo.city.CityItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.BufferedReader
import java.io.InputStreamReader
import kotlin.concurrent.thread

// 需要获取应用上下文, 所以使用AndroidViewModel
class WeatherViewModel(application: Application) : AndroidViewModel(application) {
    // 内部使用
    private val _cities:MutableLiveData<List<CityItem>> = MutableLiveData()

    // 外部调用
    val cities = _cities

    // 数据初始化
    init {
        thread {
            //获取资源文件（原始）
            val citycode = readFileFromRaw(R.raw.citycode)
            val gson = Gson()
            val CityType = object : TypeToken<List<CityItem>>(){}.type
            var cts:List<CityItem> = gson.fromJson(citycode, CityType)
            cts = cts.filter { it.city_code != "" }
            // 当前位于子线程, 必须使用postValue
            _cities.postValue((cts))
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