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
import com.aston.phenders.time01.API.putTime
import com.aston.phenders.time01.R
import com.aston.phenders.time01.activities.MainActivity
import com.aston.phenders.time01.database.DatabaseHelper
import com.aston.phenders.time01.database.TimeTable
import com.aston.phenders.time01.database.UserTable
import com.aston.phenders.time01.models.TimeItem
import com.aston.phenders.time01.models.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_book_time.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.toast
import org.jetbrains.anko.warn
import org.jetbrains.anko.yesButton
import java.util.*
import kotlin.collections.LinkedHashMap


class BookTimeFragment : Fragment(), AnkoLogger {

    var date: Calendar = Calendar.getInstance()

    var startDateYear = date.get(Calendar.YEAR)
    var endDateYear = startDateYear

    var startDateMonth = date.get(Calendar.MONTH)
    var endDateMonth = startDateMonth

    var startDateDay = date.get(Calendar.DAY_OF_MONTH)
    var endDateDay = startDateDay

    var initialDisplayMonth = startDateMonth + 1

    var projectCode: EditText? = null
    var projectTask: EditText? = null
    var categorySpinner: Spinner? = null

    var numOfHours: EditText? = null
    var includeWeekends: CheckBox? = null
    var adapter: ArrayAdapter<CharSequence>? = null

    lateinit var user: User


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)


        var view = inflater.inflate(R.layout.fragment_book_time, container, false)

        projectCode = view.findViewById(R.id.text_input_project_code)
        projectTask = view.findViewById(R.id.text_input_project_task)
        val buttonStartDate = view.findViewById<Button>(R.id.button_book_start_date)
        val buttonEndDate = view.findViewById<Button>(R.id.button_book_end_date)
        val selectedStartDate = view.findViewById<TextView>(R.id.start_date_selected_date)
        val selectedEndDate = view.findViewById<TextView>(R.id.end_date_selected_date)
        numOfHours = view.findViewById(R.id.num_input_hours)
        includeWeekends = view.findViewById<CheckBox>(R.id.checkbox_include_weekends)
        val bookTimeButton = view.findViewById<Button>(R.id.button_book_time)

        adapter = ArrayAdapter.createFromResource(context,
                R.array.categories_array, android.R.layout.simple_spinner_item)

        categorySpinner = view.findViewById<Spinner>(R.id.category_spinner)

        adapter!!.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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

                verifyDateSelection(startDateYear, startDateMonth, startDateDay, endDateYear, endDateMonth, endDateDay)

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
                verifyDateSelection(startDateYear, startDateMonth, startDateDay, endDateYear, endDateMonth, endDateDay)

            }, endDateYear, endDateMonth, endDateDay)
            dpd.show()

        }

        bookTimeButton?.setOnClickListener {

            var monthsArray = (resources.getStringArray(R.array.months_array))

            var timeItem = TimeItem()
            timeItem.projectCode = projectCode!!.text.toString()
            timeItem.projectTask = projectTask!!.text.toString()
            timeItem.category = categorySpinner!!.selectedItem.toString()
            timeItem.year = startDateYear.toString()
            timeItem.month = monthsArray[startDateMonth]
            timeItem.startDate = startDateDay.toLong()
            timeItem.endDate = endDateDay.toLong()



            addTimeBooking(timeItem, startDateMonth, includeWeekends!!.isChecked, numOfHours!!.text.toString().toFloat(), user.userID!!)


        }

        //set text fields and verify dates when view is loaded
        selectedStartDate.text = ("" + startDateDay + "/" + (startDateMonth + 1) + "/" + startDateYear)
        selectedEndDate.text = ("" + endDateDay + "/" + (endDateMonth + 1) + "/" + endDateYear)


        return view
    }

    override fun onResume() {
        super.onResume()

        if ((activity as MainActivity).loadPrefs) { // set initial values
            val db = DatabaseHelper(activity!!.applicationContext)
            val userTable = UserTable(db)
            user = userTable.getUser()

            projectCode!!.setText(user.projectCode)
            projectTask!!.setText(user.projectTask)
            categorySpinner!!.setSelection(adapter!!.getPosition(user.category))
            numOfHours!!.setText(user.workingHours.toString())
            includeWeekends!!.isChecked = user.worksWeekends!!.toBoolean()

            warn("FIRST TIME RUNNING, Loaded User Prefs")

            (activity as MainActivity).loadPrefs = false
        }

        verifyDateSelection(startDateYear, startDateMonth, startDateDay, endDateYear, endDateMonth, endDateDay)


    }


    private fun addTimeBooking(timeItem: TimeItem, month: Int, weekends: Boolean, hours: Float, userID: Int) {

        var dates: LinkedHashMap<Int, Float> = LinkedHashMap()
        var hoursTotal = 0F

        if (weekends) {

            for (i in timeItem.startDate!!..timeItem.endDate!!) {
                dates.put(i.toInt(), hours)
                hoursTotal += hours
            }
        } else {

            var date: Calendar = Calendar.getInstance()
            date.set(Calendar.YEAR, timeItem.year!!.toInt())
            date.set(Calendar.MONTH, month) //set month and year outside of for loop as will not change

            for (i in timeItem.startDate!!..timeItem.endDate!!) {

                date.set(Calendar.DAY_OF_MONTH, i.toInt())

                if (date.get(Calendar.DAY_OF_WEEK) != 7 && date.get(Calendar.DAY_OF_WEEK) != 1) { // 7 & 1  == Sat & Sun
                    dates.put(i.toInt(), hours)
                    hoursTotal += hours
                }
            }

        }

        if (hoursTotal != 0F) {

            timeItem.dates = dates
            timeItem.quantity = hoursTotal

            val db = DatabaseHelper(activity!!.applicationContext)
            val tt = TimeTable(db)

            tt.addNewTime(timeItem)


            timeItem.timeId = tt.getLastID()
            val api: putTime = putTime()
            api.putTime(timeItem, userID)

            toast("Time Recorded")
            //returnToSummary couples fragment to activity- bad form but acceptable as fragment will not be reused in this scenario
            (activity as MainActivity).returnToSummary()


        } else

            alert("The selected options do not book any hours") {
                yesButton { }
            }.show()


    }

    private fun verifyDateSelection(startYear: Int, startMonth: Int, startDate: Int, endYear: Int, endMonth: Int, endDate: Int) {


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

    fun test(timeItem: TimeItem) {

        var dates: LinkedHashMap<Int, Float> = LinkedHashMap()
        var hours = 7.5F
        val gson = Gson()


        for (i in timeItem.startDate!!..timeItem.endDate!!) {

            dates.put(i.toInt(), hours)
            warn(dates)


        }
        warn("GSON INCOMING")
        var datesJSON: String = gson.toJson(dates)
        warn(datesJSON)
        warn("AND BACK TO NORMAL")

        var datesNotJSON = gson.fromJson<LinkedHashMap<Int, Float>>(datesJSON,
                object : TypeToken<LinkedHashMap<Int, Float>>() {}.type)


        warn(datesNotJSON)
        datesNotJSON.remove(30)
        datesNotJSON.put(30, 5F)
        datesNotJSON.putAll(datesNotJSON.toSortedMap())
        warn(datesNotJSON)
        datesJSON.toList()


    }

}

