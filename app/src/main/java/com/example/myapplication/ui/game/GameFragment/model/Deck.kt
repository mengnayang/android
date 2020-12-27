package com.example.myapplication.ui.GameFragment.model

import java.util.Random

class Deck() {
    private val cards = mutableListOf<Card>()
    private val r = Random()
    init {
        for (suit in Card.validSuits) {
            for (rank in Card.rankStrings) {
                val card = Card(suit=suit,rank=rank)
                cards.add(card)
            }
        }
    }

    fun drawRandomCard(): Card? {
        var randomCard: Card? = null
        if (cards.size > 0) {
            randomCard = cards.removeAt(r.nextInt(cards.size))
        }
        return randomCard
    }
}