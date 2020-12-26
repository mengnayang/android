package com.example.myapplication.ui.chat

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_chat.*
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.lang.Exception

class ChatFragment : Fragment() {

    lateinit var msgList:MutableList<Msg>

    companion object {
        fun newInstance() = ChatFragment()
    }

    private lateinit var viewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ChatViewModel::class.java)

        val msgs = loadData()

        if(msgs == null) {
            msgList = mutableListOf()
        } else {
            msgList = msgs
        }

        //建立适配器
        val adapter = MessageAdapter(msgList)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.chat_recyclerView)
        recyclerView?.adapter = adapter
        val layoutManager = LinearLayoutManager(activity)
        recyclerView?.layoutManager = layoutManager


        //发送消息
        chat_sendMsg.setOnClickListener {
            //val editMessage = editMsg.text.toString()
            val editMessage = view?.findViewById<EditText>(R.id.chat_editMsg)?.text.toString()
            val msg = addMsg(editMessage)
            msgList.add(msg)
            adapter.notifyItemInserted(msgList.size-1)
            recyclerView!!.scrollToPosition(msgList.size-1)

        }
    }

    //定义消息类
    data class Msg(val content:String, val type: Int): Serializable {
        companion object{
            const val RECEIVE = 0
            const val SEND = 1
        }
    }

    //添加消息
    fun addMsg(msg: String):Msg {
        val count = (1..10).random()
        val builder = StringBuffer()
        for (i in 0..count) {
            builder.append(msg)
        }
        Log.d("info",builder.toString())
        return Msg(builder.toString(),(0..1).random())
    }

    //建立消息适配器
    class MessageAdapter(val list:MutableList<Msg>): RecyclerView.Adapter<MessageAdapter.ViewHolder>() {
        class ViewHolder(view:View):RecyclerView.ViewHolder(view){
            val leftMsg: TextView
            val rightMsg: TextView
            init {
                leftMsg = view.findViewById(R.id.chat_message_left)
                rightMsg = view.findViewById(R.id.chat_message_right)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout,parent,false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val msg = list.get(position)
            if (msg.type == Msg.RECEIVE) {
                holder.rightMsg.visibility = View.GONE
                holder.leftMsg.visibility = View.VISIBLE
                holder.leftMsg.text = msg.content
            } else {
                holder.leftMsg.visibility = View.GONE
                holder.rightMsg.visibility = View.VISIBLE
                holder.rightMsg.text = msg.content
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }
    }

    //停止时，保存数据
    override fun onStop() {
        super.onStop()
        saveData()
    }

    //保存数据
    fun saveData(){
        val output = activity?.openFileOutput("chatData", Context.MODE_PRIVATE)
        val objectOutputStram = ObjectOutputStream(output)
        objectOutputStram.writeObject(msgList)
        objectOutputStram.close()
        output?.close()
    }

    //加载数据
    fun loadData():MutableList<Msg>?{
        try {
            val input = activity?.openFileInput("chatData")
            val objectInputStram = ObjectInputStream(input)
            val msgs = objectInputStram.readObject() as MutableList<Msg>
            objectInputStram.close()
            input?.close()
            return msgs
        } catch (e: Exception) {
            return null
        }
    }

}