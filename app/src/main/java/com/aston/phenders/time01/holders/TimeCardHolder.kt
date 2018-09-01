package com.aston.phenders.time01.holders


import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.aston.phenders.time01.R
import com.aston.phenders.time01.activities.TimeCardDatesDetail
import com.aston.phenders.time01.models.TimeItem
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.warn

class TimeCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView), AnkoLogger {
    val projectCodeTask: TextView = itemView.findViewById(R.id.project_code_task)
    var timeCategoory: TextView = itemView.findViewById(R.id.category)
    var datePeriod: TextView = itemView.findViewById(R.id.date_period)
    var quantity: TextView = itemView.findViewById(R.id.quantity)
    var timeID: Long? = null

    init {

        itemView.setOnClickListener { view: View? ->

            warn("Time ID set to: " + timeID)
            var detailPageIntent = Intent(itemView.context, TimeCardDatesDetail::class.java)
            detailPageIntent.putExtra("timeID",timeID )
            //detailPageIntent.putExtra("datePeriod", datePeriod.toString())
            itemView.context.startActivity(detailPageIntent)

        }

    }


    fun updateWithBookedTime(timeItem: TimeItem) {

        /////FIX THESE STRINGS
        projectCodeTask.text = "Code: " + timeItem.projectCode + " Task: " + timeItem.projectTask
        timeCategoory.text = timeItem.category
        datePeriod.text =
                "Period: " + timeItem.startDate.toString() + "/" + timeItem.month + " -> " + timeItem.endDate.toString() + "/" + timeItem.month
        quantity.text = timeItem.quantity.toString() + " Hours"
        timeID = timeItem.timeId

    }
}