package com.aston.phenders.time01.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aston.phenders.time01.R
import com.aston.phenders.time01.holders.TimeCardDatesDetailHolder

class TimeCardDetailAdapter : RecyclerView.Adapter<TimeCardDatesDetailHolder>() {
    val dateItems: ArrayList<Pair<Int, Float>> = ArrayList()
    var month : String = ""

    override fun getItemCount(): Int {
        return dateItems.size
    }

    override fun onBindViewHolder(holder: TimeCardDatesDetailHolder, position: Int) {
        val date = dateItems[position]
        holder.updateWithDateItem(date, month)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeCardDatesDetailHolder {
        val cardItem = LayoutInflater.from(parent.context).inflate(R.layout.time_detail_card, parent, false)
        return TimeCardDatesDetailHolder(cardItem)
    }
}