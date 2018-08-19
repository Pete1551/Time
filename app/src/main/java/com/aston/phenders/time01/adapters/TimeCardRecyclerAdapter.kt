package com.aston.phenders.time01.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aston.phenders.time01.R
import com.aston.phenders.time01.holders.TimeCardHolder
import com.aston.phenders.time01.models.TimeItem


class TimeCardRecyclerAdapter() : RecyclerView.Adapter<TimeCardHolder>() {
    val currentResults: ArrayList<TimeItem> = ArrayList()

    override fun getItemCount(): Int {
        return currentResults.size
    }

    override fun onBindViewHolder(holder: TimeCardHolder, position: Int) {
        var timeItem = currentResults[position]
        holder?.updateWithBookedTime(timeItem)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeCardHolder {

        var cardItem = LayoutInflater.from(parent.context).inflate(R.layout.time_card_item, parent, false)
        return TimeCardHolder(cardItem)
    }
}