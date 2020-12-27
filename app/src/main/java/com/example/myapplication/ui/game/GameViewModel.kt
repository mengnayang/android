package com.example.myapplication.ui.game

import androidx.lifecycle.ViewModel
import com.example.mengnayang6.GameFragment.CardAdapter
import com.example.myapplication.ui.GameFragment.model.CardMatchingGame

class GameViewModel() : ViewModel() {

    lateinit var game: CardMatchingGame
    lateinit var adapter: CardAdapter

}
