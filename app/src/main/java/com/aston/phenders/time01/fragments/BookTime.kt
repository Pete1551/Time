package com.aston.phenders.time01.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.EditText
import com.aston.phenders.time01.R
import com.aston.phenders.time01.activities.MainActivity
import com.aston.phenders.time01.database.DatabaseHelper
import com.aston.phenders.time01.database.TimeTable
import com.aston.phenders.time01.models.TimeItem
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.toast

class BookTime : Fragment(), AnkoLogger {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        var view = inflater.inflate(R.layout.fragment_book_time, container, false)

        val projectCode = view.findViewById<EditText>(R.id.text_input_project_code)
        val projectTask = view.findViewById<EditText>(R.id.text_input_project_task)
        val startDate = view.findViewById<DatePicker>(R.id.start_date_picker)
        val endDate = view.findViewById<DatePicker>(R.id.end_date_picker)
        val numOfHours = view.findViewById<EditText>(R.id.num_input_hours)
        val includeWeekends = view.findViewById<CheckBox>(R.id.checkbox_include_weekends)
        val bookTimeButton = view?.findViewById<Button>(R.id.button_book_time)

        bookTimeButton?.setOnClickListener {


            var timeItem = TimeItem()
            timeItem.projectCode = projectCode.text.toString()
            timeItem.projectTask = projectTask.text.toString()
            timeItem.month = startDate.month.toString()
            timeItem.year = startDate.year.toString()
            timeItem.startDate = startDate.dayOfMonth.toLong()
            timeItem.endDate = endDate.dayOfMonth.toLong()

            addTimeBooking(timeItem)

            //returnToSummary couples fragment to activity- bad form but acceptable as fragment will not be reused is this scenario
            (activity as MainActivity).returnToSummary()

        }

        return view
    }


    fun addTimeBooking(timeItem: TimeItem) {

        val db = DatabaseHelper(activity!!.applicationContext)
        val tt = TimeTable(db)


        tt.addNewTime(timeItem)

        toast("Time Recorded")


    }

}