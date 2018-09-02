package com.aston.phenders.time01.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.aston.phenders.time01.R
import com.aston.phenders.time01.holders.DatesDetailCardHolder
import com.aston.phenders.time01.models.TimeItem

class DatesDetailCardAdapter : RecyclerView.Adapter<DatesDetailCardHolder>() {
    val dateItems: ArrayList<Pair<Int, Float>> = ArrayList()
    var appContext: Context? = null
    var timeItem = TimeItem()


    override fun getItemCount(): Int {
        return dateItems.size
    }

    override fun onBindViewHolder(holder: DatesDetailCardHolder, position: Int) {
        val date = dateItems[position]
        holder.updateWithDateItem(date, timeItem)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DatesDetailCardHolder {
        val cardItem = LayoutInflater.from(parent.context).inflate(R.layout.time_detail_card, parent, false)
        return DatesDetailCardHolder(cardItem)
    }
}