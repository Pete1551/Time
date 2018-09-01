package com.aston.phenders.time01.holders

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.aston.phenders.time01.R
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn

class TimeCardDatesDetailHolder(itemView: View) : RecyclerView.ViewHolder(itemView), AnkoLogger {

    var dateView: TextView? = null
    var hoursView: EditText? = null


    fun updateWithDateItem(dateLine: Pair<Int, Float>, month : String) {

        dateView = itemView.findViewById(R.id.time_date)
        hoursView = itemView.findViewById(R.id.date_hours)
        dateView!!.text = dateLine.first.toString() + "/" + month + "."
        hoursView!!.setText(dateLine.second.toString())
    }
}