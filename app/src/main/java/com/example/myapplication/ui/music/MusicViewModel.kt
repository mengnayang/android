package com.example.myapplication.ui.music

import android.app.Application
import android.media.MediaPlayer
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import kotlin.concurrent.thread


class MusicViewModel(application: Application) : AndroidViewModel(application) {

    val mediaPlayer = MyMediaPlayerLifecycle()
    private val _current:MutableLiveData<Int> = MutableLiveData()
    var current:LiveData<Int> = _current
    //音乐列表
    private val _musicList:MutableLiveData<List<String>> = MutableLiveData()
    val musicList:LiveData<List<String>> = _musicList
    //音乐名称列表
    private val _musicNameList:MutableLiveData<List<String>> = MutableLiveData()
    val musicNameList:LiveData<List<String>> = _musicNameList
    private var isPause = false
    // 播放进度
    private var _currentPosition:MutableLiveData<Int> = MutableLiveData()
    var currentPosition:LiveData<Int> = _currentPosition
    // 播放时长
    private val _max:MutableLiveData<Int> = MutableLiveData()
    val max:LiveData<Int> = _max
    init{
        getMusicList()
        _current.value = 0
        loadVideo()

        //子线程
        thread {
            while (true) {
                Thread.sleep(1000)
                _currentPosition.postValue(mediaPlayer.currentPosition)
                _max.postValue(mediaPlayer.duration)
            }
        }
    }
    private fun loadVideo() {
        mediaPlayer.apply{
            if (_musicList.value?.size == 0) return
            val path = _musicList.value?.get(_current.value!!)
            mediaPlayer.reset()

            Log.d("Path",path ?: "null")

            setDataSource(path)
            setOnPreparedListener{ // 准备监听
                it.start()//播放
            }
            prepareAsync()

            //播放完自动切换下一首
            setOnCompletionListener {
                _current.value = _current.value?.plus(1)
                if (_current.value!! >= musicList.value?.size!!) {
                    _current.value = 0
                }
                loadVideo()
            }
        }
    }

    fun getMusicList() {
        val musicList = mutableListOf<String>()
        val musicNames = mutableListOf<String>()
        val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getApplication<Application>().contentResolver?.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        if (cursor != null) {
            while(cursor.moveToNext()) {
                val musicPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                musicList.add(musicPath)
                val musicName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                musicNames.add(musicName)
                Log.d("Infomation","musicList=$musicPath, musicName=$musicName")
            }
            _musicList.value = musicList
            _musicNameList.value = musicNames
            cursor.close()
        }
    }

    //开始播放
    fun start() {
        loadVideo()
        Log.d("start", "start")
    }

    //上一首
    fun prev() {
        Log.d("prev","prev")
        _current.value = _current.value?.minus(1)
        if (_current.value!! <= 0) {
            _current.value = musicList.value?.size?.minus(1)
        }
        loadVideo()
    }

    //暂停播放
    fun pause() {
        Log.d("pause","pause")
        if (isPause) {
            mediaPlayer.start()
            isPause = false
        } else {
            mediaPlayer.pause()
            isPause = true
        }
    }

    //下一首
    fun next() {
        Log.d("next","next")
        _current.value = _current.value?.plus(1)
        if (_current.value!! >= musicList.value?.size!!) {
            _current.value = 0
        }
        loadVideo()
    }

    //停止播放
    fun stop() {
        Log.d("stop","stop")
        mediaPlayer.stop()
    }

    override fun onCleared() {
        super.onCleared()
        mediaPlayer.release()
    }

}

class MyMediaPlayerLifecycle:MediaPlayer(), LifecycleObserver {
    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun pausePlayer(){
        pause()
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun resumePlayer(){
        start()
    }
}