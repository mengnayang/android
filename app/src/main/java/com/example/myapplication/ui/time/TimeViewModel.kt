package com.example.myapplication.ui.time

import android.os.Handler
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class TimeViewModel : ViewModel() {
    //内部变量
    private var _second:MutableLiveData<Int> = MutableLiveData()
    private var running:Boolean = false

    //供外部监听访问的对象
    var second:LiveData<Int> = _second

    //初始化定时器
    init {
        runTimer()
    }

    //开始计数
    fun start() {
        running = true
    }

    //停止计时
    fun stop() {
        running = false
    }

    //重置计时结果
    fun reset() {
        running = false
        _second.value = 0
    }

    fun runTimer() {
        val handler = Handler()
        val runnable = object :Runnable {
            override fun run() {
                if (running) {
                    // 主线程可以用.value获取, 子线程需要用postValue获取
                    // 判断_second.value是否为空, 为空取0
                    val temp = _second.value ?: 0
                    _second.value = temp + 1
                }
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }
}