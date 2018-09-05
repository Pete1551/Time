package com.aston.phenders.time01.holders

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.aston.phenders.time01.R
import com.aston.phenders.time01.database.DatabaseHelper
import com.aston.phenders.time01.database.TimeTable
import com.aston.phenders.time01.models.TimeItem
import org.jetbrains.anko.*

class DatesDetailCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView), AnkoLogger {

    var dateView: TextView? = null
    var hoursView: EditText? = null


    fun updateWithDateItem(dateLine: Pair<Int, Float>, timeItem: TimeItem, activity: Activity) {

        dateView = itemView.findViewById(R.id.time_date)
        hoursView = itemView.findViewById(R.id.date_hours)
        dateView!!.text = dateLine.first.toString() + "/" + timeItem.month + "."
        hoursView!!.setText(dateLine.second.toString())
        val updateButton: ImageView = itemView.findViewById(R.id.update_button)
        val deleteButton: ImageView = itemView.findViewById(R.id.delete_button)

        updateButton.setOnClickListener {
            timeItem.updateDateHours(dateLine.first, hoursView?.text.toString().toFloat())
            val db = DatabaseHelper(itemView.context)
            val tt = TimeTable(db)
            tt.updateTimeItem(timeItem)
            activity.recreate()
            Toast.makeText(itemView.context, "Updated", Toast.LENGTH_SHORT).show()

        }
        deleteButton.setOnClickListener {

            with(itemView.context) {
                alert("Are you sure you want to remove this date?")
                {
                    title = "Confirm"
                    yesButton {
                        timeItem.removeDate(dateLine.first)
                        val db = DatabaseHelper(itemView.context!!)
                        val tt = TimeTable(db)
                        tt.updateTimeItem(timeItem)
                        activity.recreate()
                        toast("Deleted")


                    }
                    noButton { }
                }.show()

            }
        }
    }
}