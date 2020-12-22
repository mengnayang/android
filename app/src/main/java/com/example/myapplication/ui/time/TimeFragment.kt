package com.example.myapplication.ui.time

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_time.*

class TimeFragment : Fragment() {

    //获取对应的viewModel
    lateinit var viewModel: TimeViewModel

    companion object {
        fun newInstance() = TimeFragment()
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_time, container, false)
    }

    //此时Activity, viewModel均有
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(TimeViewModel::class.java)

        //监听viewModel中second的变化
        viewModel.second.observe(viewLifecycleOwner, Observer {
            val hours = it / 3600
            val minute = it / 60
            val second = it % 60
            textView_time.text = String.format("%02d:%02d:%02d", hours, minute, second)
        })

        //调用viewModel中的开始按钮
        time_btn_start.setOnClickListener {
            viewModel.start()
            //此时不能用this, 因为此刻不处于Activity中, 需要用activity才行
            Toast.makeText(activity, "计时开始", Toast.LENGTH_SHORT).show()
        }

        //调用viewModel中的停止按钮
        time_btn_stop.setOnClickListener {
            viewModel.stop()
            Toast.makeText(activity, "计时停止", Toast.LENGTH_SHORT).show()
        }

        //调用viewModel中的重置按钮
        time_btn_reset.setOnClickListener {
            viewModel.reset()
            Toast.makeText(activity, "计时重置", Toast.LENGTH_SHORT).show()
        }
    }
}