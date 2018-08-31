package com.aston.phenders.time01.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import com.aston.phenders.time01.R
import com.aston.phenders.time01.activities.MainActivity
import com.aston.phenders.time01.database.DatabaseHelper
import com.aston.phenders.time01.database.TimeTable
import com.aston.phenders.time01.database.UserTable
import com.aston.phenders.time01.models.TimeItem
import kotlinx.android.synthetic.main.fragment_book_time.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.warn
import java.util.*
import kotlin.collections.ArrayList


class BookTime : Fragment(), AnkoLogger {

    var date: Calendar = Calendar.getInstance()

    var startDateYear = date.get(Calendar.YEAR)
    var endDateYear = startDateYear

    var startDateMonth = date.get(Calendar.MONTH)
    var endDateMonth = startDateMonth

    var startDateDay = date.get(Calendar.DAY_OF_MONTH)
    var endDateDay = startDateDay

    var initialDisplayMonth = startDateMonth + 1
    val dateNowString = ("" + startDateDay + "/" + initialDisplayMonth + "/" + startDateYear)


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

        val categorySpinner = view.findViewById<Spinner>(R.id.category_spinner)
        val adapter = ArrayAdapter.createFromResource(context,
                R.array.categories_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner!!.adapter = adapter



        buttonStartDate?.setOnClickListener {

            val dpd = DatePickerDialog(context, DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                // Display Selected date in textbox
                var displayMonth = selectedMonth + 1
                selectedStartDate.text = ("" + selectedDay + "/" + displayMonth + "/" + selectedYear)

                //set values outside of interface
                startDateYear = selectedYear
                startDateMonth = selectedMonth
                startDateDay = selectedDay

                verifyTimeSelection(startDateYear, startDateMonth, startDateDay, endDateYear, endDateMonth, endDateDay)

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

                //verify entered date -> disable button if
                verifyTimeSelection(startDateYear, startDateMonth, startDateDay, endDateYear, endDateMonth, endDateDay)

            }, endDateYear, endDateMonth, endDateDay)
            dpd.show()

        }

        bookTimeButton?.setOnClickListener {

            var monthsArray = (resources.getStringArray(R.array.months_array))

            var timeItem = TimeItem()
            timeItem.projectCode = projectCode.text.toString()
            timeItem.projectTask = projectTask.text.toString()
            timeItem.category = categorySpinner.selectedItem.toString()
            timeItem.year = startDateYear.toString()
            timeItem.month = monthsArray[startDateMonth]
            timeItem.startDate = startDateDay.toLong()
            timeItem.endDate = endDateDay.toLong()



            addTimeBooking(timeItem, startDateMonth, includeWeekends.isChecked, numOfHours.text.toString().toFloat())

            //returnToSummary couples fragment to activity- bad form but acceptable as fragment will not be reused in this scenario
            (activity as MainActivity).returnToSummary()

        }

        //set text fields and verify dates when view is loaded
        selectedStartDate.text = ("" + startDateDay + "/" + (startDateMonth + 1) + "/" + startDateYear)
        selectedEndDate.text = ("" + endDateDay + "/" + (endDateMonth + 1) + "/" + endDateYear)


        if ((activity as MainActivity).loadPrefs) { // set initial values
            val db = DatabaseHelper(activity!!.applicationContext)
            val userTable = UserTable(db)
            var user = userTable.getUser()

            projectCode.setText(user.projectCode)
            projectTask.setText(user.projectTask)
            categorySpinner.setSelection(adapter.getPosition(user.category))
            numOfHours.setText(user.workingHours.toString())
            includeWeekends.isChecked = user.worksWeekends!!.toBoolean()

            warn("FIRST TIME RUNNING, Loaded User Prefs")

            (activity as MainActivity).loadPrefs = false
        } //else verifyTimeSelection(startDateYear, startDateMonth, startDateDay, endDateYear, endDateMonth, endDateDay)  THIS SHOULD FIX BUG BUT THROWS ERROR

        return view
    }

    override fun onResume() {
        super.onResume()


        //placed here to fix bug where by moving away from fragment with invalid dates would enable button
        verifyTimeSelection(startDateYear, startDateMonth, startDateDay, endDateYear, endDateMonth, endDateDay)


    }


    fun addTimeBooking(timeItem: TimeItem, month: Int, weekends: Boolean, hours: Float) {

        var dates: ArrayList<Int> = ArrayList()
        var hoursTotal = 0F

        if (weekends) {

            for (i in timeItem.startDate!!..timeItem.endDate!!) {
                dates.add(i.toInt())
                hoursTotal += hours
            }
        } else {

            var date: Calendar = Calendar.getInstance()
            date.set(Calendar.YEAR, timeItem.year!!.toInt())
            date.set(Calendar.MONTH, month) //set month and year outside of for loop as will not change

            for (i in timeItem.startDate!!..timeItem.endDate!!) {

                date.set(Calendar.DAY_OF_MONTH, i.toInt())

                if (date.get(Calendar.DAY_OF_WEEK) != 7 && date.get(Calendar.DAY_OF_WEEK) != 1) { // 7 & 1  == Sat & Sun
                    dates.add(i.toInt())
                    hoursTotal += hours
                }
            }

        }

        timeItem.dates = dates
        timeItem.quantity = hoursTotal

        val db = DatabaseHelper(activity!!.applicationContext)
        val tt = TimeTable(db)

        tt.addNewTime(timeItem)
        toast("Time Recorded")


    }

    private fun verifyTimeSelection(startYear: Int, startMonth: Int, startDate: Int, endYear: Int, endMonth: Int, endDate: Int) {


        date_error_msg.visibility = if ((startYear != endYear) || (startMonth != endMonth)) {
            button_book_time.isEnabled = false
            VISIBLE

        } else if (startDate > endDate) {
            button_book_time.isEnabled = false
            VISIBLE
        } else {
            button_book_time.isEnabled = true
            INVISIBLE
        }

    }

}