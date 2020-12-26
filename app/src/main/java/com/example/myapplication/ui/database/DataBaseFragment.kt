package com.example.myapplication.ui.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import kotlinx.android.synthetic.main.fragment_data_base.*

//数据库名
const val DB_NAME = "mengnayang.db"
//数据库表
const val Table_NAME = "student"
class DataBaseFragment : Fragment() {

    lateinit var db: SQLiteDatabase
    lateinit var adapter: MyRecyclerViewAdapter

    companion object {
        fun newInstance() =
            DataBaseFragment()
    }

    private lateinit var viewModel: DataBaseViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_data_base, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DataBaseViewModel::class.java)

        val openHelper = activity?.let { MyOpenHelper(it,1) }
        db = openHelper!!.writableDatabase

        val cursor = db.query(Table_NAME,null,null,null,null,null,null,null)

        //建立适配器
        adapter = MyRecyclerViewAdapter(cursor)
        database_recyclerView.adapter = adapter


        //建立布局信息
        val layoutManager = LinearLayoutManager(activity)
        //垂直布局
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        database_recyclerView.layoutManager = layoutManager


        //添加数据
        button_add.setOnClickListener {
            val contentValues = ContentValues()
            val sname = editText_sname.text.toString()
            val sage = editText_sage.text.toString()
            val ssex = radioButton_boy.isChecked
            val sdept = editText_sdept.text.toString()
            contentValues.put("sname",sname)
            contentValues.put("sage",sage)
            contentValues.put("ssex",ssex)
            contentValues.put("sdept",sdept)
            db.insert(Table_NAME,null,contentValues)
            reloadData()
        }

        //更新数据
        button_update.setOnClickListener {
            val contentValues = ContentValues()
            val sname = editText_sname.text.toString()
            val sage = editText_sage.text.toString()
            val ssex = radioButton_boy.isChecked
            val sdept = editText_sdept.text.toString()
            contentValues.put("sname",sname)
            contentValues.put("sage",sage)
            contentValues.put("ssex",ssex)
            contentValues.put("sdept",sdept)
            db.update(Table_NAME,contentValues,"sname=?", arrayOf(sname))
            reloadData()
        }

        //删除数据
        button_delete.setOnClickListener {
            val sname = editText_sname.text.toString()
            db.delete(Table_NAME,"sname=?", arrayOf(sname))
            reloadData()
        }

        //查询数据
        button_select.setOnClickListener {
            val sname = editText_sname.text.toString()
            val cursor = db.query(Table_NAME,null,"sname like '%$sname%'",null,null,null,null)
            adapter.swapCursor(cursor)
        }
    }

    //重新加载数据
    fun reloadData() {
        val cursor = db.query(Table_NAME,null,null,null,null,null,null)
        adapter.swapCursor(cursor)
    }
}

class MyOpenHelper(context: Context, version:Int): SQLiteOpenHelper(context, DB_NAME, null, version) {
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL("create table $Table_NAME(_id Integer primary key autoincrement, sname text, sage Integer," +
                "ssex boolean, sdept text)")
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        TODO("Not yet implemented")
    }
}

class MyRecyclerViewAdapter(var cursor: Cursor): RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>(){

    //判断游标是否更新
    fun swapCursor(newCursor: Cursor) {
        if (newCursor == cursor) return
        cursor.close()
        cursor = newCursor
        notifyDataSetChanged()
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val textView_sname: TextView
        val textView_sage: TextView
        val textView_ssex: TextView
        val textView_sdept: TextView
        init {
            textView_sname = view.findViewById(R.id.database_textView_sname)
            textView_sage = view.findViewById(R.id.database_textView_sage)
            textView_ssex = view.findViewById(R.id.database_textView_ssex)
            textView_sdept = view.findViewById(R.id.database_textView_sdept)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.student_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cursor.count
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        cursor.moveToPosition(position)
        holder.textView_sname.text = cursor.getString(cursor.getColumnIndex("sname"))
        holder.textView_sage.text = cursor.getString(cursor.getColumnIndex("sage"))
        holder.textView_ssex.text = if (cursor.getString(cursor.getColumnIndex("ssex")) == "1") '男'.toString() else '女'.toString()
        holder.textView_sdept.text = cursor.getString(cursor.getColumnIndex("sdept"))
    }
}
