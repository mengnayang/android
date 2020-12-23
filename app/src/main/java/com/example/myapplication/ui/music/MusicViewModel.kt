package com.example.myapplication.ui.music

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.media.MediaPlayer
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.IOException

class MusicViewModel : ViewModel() {
    // 定义音乐播放器
    private val _mediaPlayer = MediaPlayer()
    // 音乐列表
    private val _musicList:MutableList<String> = mutableListOf()
    // 音乐名称列表
    private val _musicNameList:MutableList<String> = mutableListOf()
    // 当前播放索引
    private val _current:MutableLiveData<Int> = MutableLiveData()
    // 监听播放是否暂停事件
    private var _isPause = false

    val musicList = _musicList
    val musicNameList = _musicNameList
    val current = _current

    //通知事件
    lateinit var notificationManager: NotificationManager
    lateinit var builder:Notification.Builder
    lateinit var pendingIntent: PendingIntent

    init {
        // 初始化音乐播放器
        _mediaPlayer.setOnPreparedListener {
            it.start()
        }

        // 完成后自动播放下一首
        _mediaPlayer.setOnCompletionListener {
            // 对播放索引进行判空操作
            val ct = _current.value ?: 0
            _current.value = ct + 1
            if (_current.value!! >= _musicList.size) {
                _current.value = 0
            }
            play()
        }
    }

    // 获取音乐播放列表
    fun getMusicList() {
        Log.d("MusicList","进入了getMusicList")
        val contentResolver: ContentResolver? = null
        val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            contentResolver?.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        if (cursor != null) {
            while(cursor.moveToNext()) {
                val musicPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                _musicList.add(musicPath)
                val musicName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                _musicNameList.add(musicName)
                Log.d("Infomation","musicList=$musicPath, musicName=$musicName")
            }
            cursor.close()
        }
    }

    // 音乐播放
    fun play() {
        if (_musicList.size == 0) return
        val path = _musicList[_current.value?:0]
        _mediaPlayer.reset()
        try {
            _mediaPlayer.setDataSource(path)
            _mediaPlayer.prepareAsync()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // 播放音乐
    fun start() {
        play()
    }

    // 音乐暂停
    fun pause() {
        if (_isPause) {
            _mediaPlayer.start()
            _isPause = false
        } else {
            _mediaPlayer.pause()
            _isPause = true
        }
    }

    // 停止音乐
    fun stop() {
        _mediaPlayer.stop()
    }

    // 播放上一首
    fun prev() {
        _current.value = _current.value?.minus(1)
        if (_current.value!! <= 0) {
            _current.value = _musicList.size-1
        }
        play()
    }

    // 播放下一首
    fun next() {
        _current.value = _current.value?.plus(1)
        if (_current.value!! >= _musicList.size) {
            _current.value = 0
        }
        play()
    }

}