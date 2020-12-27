package com.example.mengnayang6.GameFragment

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.ui.GameFragment.model.CardMatchingGame
import com.example.myapplication.R

//适配器
class CardAdapter(val game:CardMatchingGame):RecyclerView.Adapter<CardAdapter.ViewHolder>() {
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val cardButton:Button
        init {
            cardButton = itemView.findViewById(R.id.cardButton)
        }
    }
    private var mOnclickListenLiser:((cardIndex:Int)->Unit)? = null
    fun setOnCardClickListener(l:(cardIndex:Int)->Unit) {
        mOnclickListenLiser = l
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_layout,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return game.cards.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val card = game.cardAtIndex(position)
        holder.cardButton.isEnabled = !card.isMatched
        if(card.isChosen) {
            holder.cardButton.text = card.toString()
            holder.cardButton.setBackgroundColor(Color.WHITE)
        } else {
            holder.cardButton.text = ""
            holder.cardButton.setBackgroundResource(R.drawable.game_bg)
        }

        holder.cardButton.setOnClickListener {
            mOnclickListenLiser?.invoke(position)
        }
    }


}