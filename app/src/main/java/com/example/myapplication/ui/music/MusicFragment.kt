 package com.example.myapplication.ui.music

import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.media.MediaPlayer
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_music.*

 //音乐播放功能
//记得在清单中注册（私有权限）
 const val ChannelID = "MyChannel"
 class MusicFragment : Fragment() {

    //初始化viewModel
    lateinit var viewModel: MusicViewModel
//     //音乐播放器
//     val mediaPlayer = MediaPlayer()
//     //音乐列表
//     val musicList = mutableListOf<String>()
//     //音乐名称列表
//     val musicNameList = mutableListOf<String>()
//     //当前播放音乐索引
//     var current = 0
//     //监听暂停事件
//     var isPause = false
//
//     //通知事件
//     lateinit var notificationManager:NotificationManager
//     lateinit var builder: Notification.Builder
//     lateinit var pendingIntent:PendingIntent
//
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
//        viewModel = ViewModelProvider(this).get(MusicViewModel::class.java)
//        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)).get(MusicViewModel::class.java)
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(MusicViewModel::class.java)

        viewModel.progressBarVisibility.observe(viewLifecycleOwner, Observer {
            seekBar.visibility = it;
        })

        lifecycle.addObserver(viewModel.mediaPlayer) //感应生命周期

        //权限申请
        if (activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED) {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0) }
        } else {
            viewModel.getMusicList()
        }
//
//
//        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                //用户是否触碰
//                if (fromUser) {
//                    mediaPlayer.seekTo(progress)
//                }
//            }
//
//            override fun onStartTrackingTouch(p0: SeekBar?) {
//            }
//
//            override fun onStopTrackingTouch(p0: SeekBar?) {
//            }
//        })
//
//        //子线程
//        thread {
//            while (true) {
//                Thread.sleep(1000)
//                seekBar.max = mediaPlayer.duration
//                seekBar.progress = mediaPlayer.currentPosition
//            }
//        }
//
        music_btn_start.setOnClickListener {
            viewModel.start()
        }

        music_btn_pause.setOnClickListener {
            viewModel.pause()
        }

        music_btn_stop.setOnClickListener {
            viewModel.stop()
        }

        music_btn_prev.setOnClickListener {
            viewModel.prev()
        }

        music_btn_next.setOnClickListener {
            viewModel.next()
        }

//        //返回音乐播放列表
//        val intent = Intent(activity, MainActivity::class.java)
//        pendingIntent = PendingIntent.getActivity(activity, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    }

//
//         //通知管理器
//         notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//             val notificationChannel = NotificationChannel(ChannelID, "this is my music channel", NotificationManager.IMPORTANCE_DEFAULT)
//             notificationManager.createNotificationChannel(notificationChannel)
//             builder = Notification.Builder(activity, ChannelID)
//         } else {
//             builder = Notification.Builder(activity)
//         }
//
//         val notification = builder.setSmallIcon(R.drawable.ic_launcher_foreground)
//                 .setContentTitle("音乐播放列表")
//                 .setContentText(musicNameList[current])
//                 .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.music_star))
//                 .setContentIntent(pendingIntent)
//                 .build()
//
//         notificationManager.notify(1, notification)
//     }
//
     override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults)
         viewModel.getMusicList()
     }
}