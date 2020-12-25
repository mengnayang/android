 package com.example.myapplication.ui.music

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_music.*
import java.io.IOException
import kotlin.concurrent.thread

 //音乐播放功能
//记得在清单中注册（私有权限）
 const val ChannelID = "MyChannel"
 class MusicFragment : Fragment() {

    //初始化viewModel
    lateinit var viewModel: MusicViewModel
     //音乐播放器
     val mediaPlayer = MediaPlayer()
     //音乐列表
     val musicList = mutableListOf<String>()
     //音乐名称列表
     val musicNameList = mutableListOf<String>()
     //当前播放音乐索引
     var current = 0
     //监听暂停事件
     var isPause = false

     //通知事件
     lateinit var notificationManager:NotificationManager
     lateinit var builder: Notification.Builder
     lateinit var pendingIntent:PendingIntent

    companion object {
        fun newInstance() = MusicFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_music, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MusicViewModel::class.java)

        mediaPlayer.setOnPreparedListener {
            it.start()
        }

        //播放完自动切换下一首
        mediaPlayer.setOnCompletionListener {
            current++
            if (current >= musicList.size) {
                current = 0
            }
            play()
        }

        //权限申请
        if (activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED) {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0) }
        } else {
            getMusicList()
        }


        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //用户是否触碰
                if (fromUser) {
                    mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

        //子线程
        thread {
            while (true) {
                Thread.sleep(1000)
                seekBar.max = mediaPlayer.duration
                seekBar.progress = mediaPlayer.currentPosition
            }
        }

        music_btn_start.setOnClickListener {
            start()
        }

        music_btn_pause.setOnClickListener {
            pause()
        }

        music_btn_stop.setOnClickListener {
            stop()
        }

        music_btn_prev.setOnClickListener {
            prev()
        }

        music_btn_next.setOnClickListener {
            next()
        }

        //返回音乐播放列表
        val intent = Intent(activity, MainActivity::class.java)
        pendingIntent = PendingIntent.getActivity(activity, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

     //开始播放
     fun start() {
         play()
         Log.d("start", "start")
     }

     //上一首
     fun prev() {
         Log.d("prev","prev")
         current--
         if (current <= 0) {
             current = musicList.size-1
         }
         play()
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
         current++
         if (current >= musicList.size) {
             current = 0
         }
         play()
     }

     //停止播放
     fun stop() {
         Log.d("stop","stop")
         mediaPlayer.stop()
     }

     fun play() {
         if (musicList.size == 0) return
         val path = musicList[current]
         mediaPlayer.reset()
         try {
             mediaPlayer.setDataSource(path)
             mediaPlayer.prepareAsync()
             textView_music_musicName.text = musicNameList[current]
             textView_music_count.text = "${current+1}/${musicList.size}"
         } catch (e: IOException) {
             e.printStackTrace()
         }

         //通知管理器
         notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             val notificationChannel = NotificationChannel(ChannelID, "this is my music channel", NotificationManager.IMPORTANCE_DEFAULT)
             notificationManager.createNotificationChannel(notificationChannel)
             builder = Notification.Builder(activity, ChannelID)
         } else {
             builder = Notification.Builder(activity)
         }

         val notification = builder.setSmallIcon(R.drawable.ic_launcher_foreground)
                 .setContentTitle("音乐播放列表")
                 .setContentText(musicNameList[current])
                 .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.music_star))
                 .setContentIntent(pendingIntent)
                 .build()

         notificationManager.notify(1, notification)
     }

     override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults)
         getMusicList()
     }

     private fun getMusicList() {
         val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             activity?.contentResolver?.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null)
         } else {
             TODO("VERSION.SDK_INT < O")
         }
         if (cursor != null) {
             while(cursor.moveToNext()) {
                 val musicPath = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                 musicList.add(musicPath)
                 val musicName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                 musicNameList.add(musicName)
                 Log.d("Infomation","musicList=$musicPath, musicName=$musicName")
             }
             cursor.close()
         }
     }

     //释放资源
     override fun onDestroy() {
         super.onDestroy()
         mediaPlayer.release()
     }
}