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
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.myapplication.MainActivity
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_music.*
import kotlin.concurrent.thread

 //音乐播放功能
//记得在清单中注册（私有权限）
 const val ChannelID = "MyChannel"
 class MusicFragment : Fragment() {

    //初始化viewModel
    lateinit var viewModel: MusicViewModel


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
        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory(requireActivity().application)).get(MusicViewModel::class.java)


        lifecycle.addObserver(viewModel.mediaPlayer) //感应生命周期

        //权限申请
        if (activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED) {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0) }
        } else {
            viewModel.getMusicList()
        }

        var max_length = 0
        viewModel.max.observe(viewLifecycleOwner, Observer {
            music_seekBar.max = it
            max_length = it
        })

        viewModel.currentPosition.observe(viewLifecycleOwner, Observer {
            music_seekBar.progress = it
            var leave_length = max_length - it
            val minute = leave_length / 60000
            val second = leave_length % 60000 / 1000
            textView_music_time.text = String.format("%02d:%02d", minute, second)
        })




        music_seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //用户是否触碰
                if (fromUser) {
                    viewModel.mediaPlayer.seekTo(progress)
                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }
        })

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

        //返回音乐播放列表
        val intent = Intent(activity, MainActivity::class.java)
        pendingIntent = PendingIntent.getActivity(activity, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        //通知管理器
        notificationManager = activity?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(ChannelID, "this is my music channel", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(notificationChannel)
            builder = Notification.Builder(activity, ChannelID)
        } else {
            builder = Notification.Builder(activity)
        }

        // 实时监听播放到哪一首
        viewModel.current.observe(viewLifecycleOwner, Observer {
            textView_music_count.text = "${it+1}/${viewModel.musicList.value?.size}"
            textView_music_musicName.text = viewModel.musicNameList.value?.get(it)
            val notification = builder.setSmallIcon(R.drawable.music_small_icon)
                .setContentTitle("音乐播放列表")
                .setContentText(viewModel.musicNameList.value?.get(it))
                .setLargeIcon(BitmapFactory.decodeResource(resources,R.drawable.music_star))
                .setContentIntent(pendingIntent)
                .build()
            notificationManager.notify(1, notification)
        })
    }

     override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
         super.onRequestPermissionsResult(requestCode, permissions, grantResults)
         viewModel.getMusicList()
     }
}