package com.aston.phenders.time01.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import com.aston.phenders.time01.R
import com.aston.phenders.time01.activities.MainActivity
import com.aston.phenders.time01.database.DatabaseHelper
import com.aston.phenders.time01.database.TimeTable
import com.aston.phenders.time01.models.TimeItem
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.warn
import java.util.*


class BookTime : Fragment(), AnkoLogger {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)

        var view = inflater.inflate(R.layout.fragment_book_time, container, false)

        val projectCode = view.findViewById<EditText>(R.id.text_input_project_code)
        val projectTask = view.findViewById<EditText>(R.id.text_input_project_task)
        val buttonStartDate = view.findViewById<Button>(R.id.button_book_start_date)
        val buttonEndDate = view.findViewById<Button>(R.id.button_book_end_date)
        val selectedStartDate = view.findViewById<TextView>(R.id.start_date_selected_date)
        val selectedEndDate = view.findViewById<TextView>(R.id.end_date_selected_date)
        val numOfHours = view.findViewById<EditText>(R.id.num_input_hours)
        val includeWeekends = view.findViewById<CheckBox>(R.id.checkbox_include_weekends)
        val bookTimeButton = view.findViewById<Button>(R.id.button_book_time)

        // val sdf = SimpleDateFormat("dd/MM/yyyy")
        // val dateNow = sdf.format(Date())


        var date: Calendar = Calendar.getInstance()

        var startDateYear = date.get(Calendar.YEAR)
        var endDateYear = startDateYear

        var startDateMonth = date.get(Calendar.MONTH)
        var endDateMonth = startDateMonth

        var startDateDay = date.get(Calendar.DAY_OF_MONTH)
        var endDateDay = startDateDay

        var initialDisplayMonth = startDateMonth + 1
        val dateNowString = ("" + startDateDay + "/" + initialDisplayMonth + "/" + startDateYear)

        selectedStartDate.text = dateNowString
        selectedEndDate.text = dateNowString


        buttonStartDate?.setOnClickListener {

            val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                // Display Selected date in textbox
                var displayMonth = selectedMonth + 1
                selectedStartDate.text = ("" + selectedDay + "/" + displayMonth + "/" + selectedYear)

                //set values outside of interface
                startDateYear = selectedYear
                startDateMonth = selectedMonth
                startDateDay = selectedDay


            }, startDateYear, startDateMonth, startDateDay)
            dpd.show()

        }
        buttonEndDate?.setOnClickListener {

            val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                // Display Selected date in textbox
                var displayMonth = selectedMonth + 1
                selectedEndDate.text = ("" + selectedDay + "/" + displayMonth + "/" + selectedYear)

                //set values outside of interface
                endDateYear = selectedYear
                endDateMonth = selectedMonth
                endDateDay = selectedDay


            }, endDateYear, endDateMonth, endDateDay)
            dpd.show()

        }

        bookTimeButton?.setOnClickListener {


            var timeItem = TimeItem()
            timeItem.projectCode = projectCode.text.toString()
            timeItem.projectTask = projectTask.text.toString()
            timeItem.year = startDateYear.toString()
            timeItem.month = (startDateMonth+1).toString()
            timeItem.startDate = startDateDay.toLong()
            timeItem.endDate = endDateDay.toLong()


            /*
            timeItem.month = startDate.month.toString()
            timeItem.year = startDate.year.toString()
            timeItem.startDate = startDate.dayOfMonth.toLong()
            timeItem.endDate = endDate.dayOfMonth.toLong()  */

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