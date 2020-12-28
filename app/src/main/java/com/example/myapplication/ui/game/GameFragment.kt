package com.example.myapplication.ui.game

import android.content.Context
import android.content.res.Configuration
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mengnayang6.GameFragment.CardAdapter
import com.example.myapplication.R
import com.example.myapplication.ui.GameFragment.model.CardMatchingGame
import kotlinx.android.synthetic.main.fragment_game.*
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.lang.Exception

class GameFragment : Fragment() {


    companion object {
        fun newInstance() = GameFragment()
    }

    private lateinit var viewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_game, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        val game1 = loadData()
        if(game1 == null) {
            viewModel.game = CardMatchingGame(24)
        } else {
            viewModel.game = game1
        }

        viewModel.adapter = CardAdapter(viewModel.game)
        updateUI()
        val recyclerView = view?.findViewById<RecyclerView>(R.id.recycleView1)
        recyclerView?.adapter = viewModel.adapter

        val config = resources.configuration
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView?.layoutManager = GridLayoutManager(activity,12)
        } else {
            recyclerView?.layoutManager = GridLayoutManager(activity,4)
        }

        viewModel.adapter.setOnCardClickListener {
            viewModel.game.chooseCardAtIndex(it)
            updateUI()
        }
        reset.setOnClickListener {
            viewModel.game.reset()
            updateUI()
        }

    }

    fun updateUI(){
        val score = view?.findViewById<TextView>(R.id.Score)
        viewModel.adapter.notifyDataSetChanged()
        score?.text = String.format("%s%d",getString(R.string.Score), viewModel.game.score)
        score?.text = getString(R.string.Score) + viewModel.game.score
    }

    override fun onStop() {
        super.onStop()
        saveData()
    }

    //保存数据
    fun saveData(){
        try {
            val outputStream = activity?.openFileOutput("gameData", Context.MODE_PRIVATE)
            ObjectOutputStream(outputStream).use{
                it.writeObject(viewModel.game)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    // 加载数据
    fun loadData(): CardMatchingGame?{
        try {
            val inputStream = activity?.openFileInput("gameData")
            val input = ObjectInputStream(inputStream)
            val game = input.readObject() as CardMatchingGame
            input.close()
            inputStream?.close()
            return game
        } catch (e: Exception) {
            return null
        }
    }
}
