package com.example.myapplication.ui.music

import android.Manifest
import android.content.pm.PackageManager
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

class MusicFragment : Fragment() {

    //初始化viewModel
    lateinit var viewModel: MusicViewModel

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

//        //权限申请
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0)
//        } else {
//            getMusicList()
//        }

        //权限申请
        if (activity?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.READ_EXTERNAL_STORAGE) } != PackageManager.PERMISSION_GRANTED) {
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 0) }
        } else {
            viewModel.getMusicList()
        }

        viewModel.current.observe(viewLifecycleOwner, Observer {
            textView_music_musicName.text = viewModel.musicNameList[it]
            textView_music_count.text = "${it+1}/${viewModel.musicList.size}"
        })

        // 开始播放
        music_btn_start.setOnClickListener {
            viewModel.start()
        }

        // 停止播放
        music_btn_stop.setOnClickListener {
            viewModel.stop()
        }

        // 暂停播放
        music_btn_pause.setOnClickListener {
            viewModel.pause()
        }

        // 下一首
        music_btn_next.setOnClickListener {
            viewModel.next()
        }

        // 上一首
        music_btn_prev.setOnClickListener {
            viewModel.prev()
        }

    }

    // 成功获取权限，进行音乐列表获取
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        viewModel.getMusicList()
    }

}