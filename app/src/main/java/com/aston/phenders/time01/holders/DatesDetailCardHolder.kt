package com.aston.phenders.time01.holders

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.aston.phenders.time01.R
import com.aston.phenders.time01.api.PutTime
import com.aston.phenders.time01.models.TimeItem
import com.aston.phenders.time01.repositories.DatabaseHelper
import com.aston.phenders.time01.repositories.TimeTable
import org.jetbrains.anko.*

class DatesDetailCardHolder(itemView: View) : RecyclerView.ViewHolder(itemView), AnkoLogger {

    var dateView: TextView? = null
    var hoursView: EditText? = null


    fun updateWithDateItem(dateLine: Pair<Int, Float>, timeItem: TimeItem, activity: Activity, userID: Int) {

        dateView = itemView.findViewById(R.id.time_date)
        hoursView = itemView.findViewById(R.id.date_hours)
        dateView!!.text = dateLine.first.toString() + "/" + timeItem.month + "."
        hoursView!!.setText(dateLine.second.toString())
        val updateButton: ImageView = itemView.findViewById(R.id.update_button)
        val deleteButton: ImageView = itemView.findViewById(R.id.delete_button)

        updateButton.setOnClickListener {
            timeItem.updateDateHours(dateLine.first, hoursView?.text.toString().toFloat())

            val api = PutTime()
            val success = api.putTime(timeItem, userID)

            if (success) {
                val db = DatabaseHelper(itemView.context)
                val tt = TimeTable(db)
                tt.updateTimeItem(timeItem)
                activity.recreate()
                Toast.makeText(itemView.context, "Updated", Toast.LENGTH_SHORT).show()
            } else {
                with(itemView.context) {
                    alert("Server Communication failed, please check internet connection or contact an administrator. This update has not been saved.") {
                        yesButton { }
                    }.show()
                }
            }


        }
        deleteButton.setOnClickListener {

            with(itemView.context) {
                alert("Are you sure you want to remove this date?")
                {
                    title = "Confirm"
                    yesButton {

                        val msg = timeItem.removeDate(dateLine.first)
                        val api = PutTime()
                        val success = api.putTime(timeItem, userID)

                        if (success) {
                            val db = DatabaseHelper(itemView.context!!)
                            val tt = TimeTable(db)
                            tt.updateTimeItem(timeItem)
                            activity.recreate()
                            toast(msg)

                        } else {
                            //warn the user and put the item back
                            timeItem.dates!!.put(dateLine.first, dateLine.second)
                            alert("Server Communication failed, please check internet connection or contact an administrator. This update has not been saved.") {
                                yesButton { }
                            }.show()
                        }
                    }
                    noButton { }
                }.show()

            }
        }
    }
}