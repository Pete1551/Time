package com.aston.phenders.time01.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.InputType
import android.view.WindowManager
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.aston.phenders.time01.R
import com.aston.phenders.time01.adapters.DatesDetailCardAdapter
import com.aston.phenders.time01.api.DeleteTime
import com.aston.phenders.time01.api.PutTime
import com.aston.phenders.time01.models.TimeItem
import com.aston.phenders.time01.repositories.DatabaseHelper
import com.aston.phenders.time01.repositories.TimeTable
import com.aston.phenders.time01.repositories.UserTable
import org.jetbrains.anko.*
import java.util.*


class DatesDetailActivity : AppCompatActivity(), AnkoLogger {


    private var detailsView: RecyclerView? = null
    private var dateDetailsAdapter = DatesDetailCardAdapter()

    var projectCodeTask: TextView? = null
    var timeCategory: TextView? = null
    var datePeriod: TextView? = null
    var quantity: TextView? = null
    var backButton: ImageView? = null
    var deleteButton: ImageView? = null
    var fab: ImageButton? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        setContentView(R.layout.activity_dates_detail)

        detailsView = findViewById(R.id.time_detail_recycler)
        detailsView!!.layoutManager = LinearLayoutManager(this)
        detailsView!!.adapter = dateDetailsAdapter


        projectCodeTask = findViewById(R.id.detail_project_code_task)
        timeCategory = findViewById(R.id.detail_category)
        datePeriod = findViewById(R.id.detail_date_period)
        quantity = findViewById(R.id.detail_quantity)
        backButton = findViewById(R.id.detail_back_button)
        deleteButton = findViewById(R.id.full_delete_button)
        fab = findViewById(R.id.fab)

    }

    override fun onResume() {
        super.onResume()

        var db = DatabaseHelper(applicationContext)
        warn("Getting TimeItem")
        val timeItem = TimeTable(db).getSingleTimeItem(intent.getLongExtra("timeID", -1))

        //userID required for API
        var user = UserTable(db).getUser()



        getDates(timeItem, user.userID)



        projectCodeTask!!.text = "Code: " + timeItem.projectCode + " Task: " + timeItem.projectTask
        timeCategory!!.text = timeItem.category
        datePeriod!!.text =
                "Period: " + timeItem.startDate.toString() + "/" + timeItem.month + " -> " + timeItem.endDate.toString() + "/" + timeItem.month
        quantity!!.text = timeItem.quantity.toString() + " Hours"



        backButton!!.setOnClickListener {
            finish()
        }
        deleteButton!!.setOnClickListener {

            alert("Are you sure you want to delete this entire booking?")
            {
                title = "Confirm"
                yesButton {

                    warn(" deleting timeID: " + timeItem.timeID)

                    val api = DeleteTime()
                    val success = api.deleteTimeItem(user.userID!!, timeItem.timeID!!.toInt())

                    if (success) {
                        val tt = TimeTable(db)
                        tt.deleteTimeItem(timeItem.timeID!!)
                        finish()
                    } else {
                        alert("Server Communication failed, please check internet connection or contact an administrator. This time has not been removed.") {
                            yesButton { }
                        }.show()
                    }
                }
                noButton {

                }
            }.show()


        }

        fab!!.setOnClickListener { _ ->

            alert {
                title = "Add New Date"

                customView {
                    linearLayout {
                        val dateEntry = editText {
                            inputType = InputType.TYPE_CLASS_NUMBER
                            hint = "Date"

                        }

                        val hoursEntry = editText() {
                            inputType = (InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL)
                            hint = "Hours"
                        }
                        padding = dip(16)

                        positiveButton("Add Date") {

                            if (dateEntry.text.toString() == "" || hoursEntry.text.toString() == "") {
                                toast("Date and Hours are mandatory")
                            } else {
                                addDate(timeItem, dateEntry.text.toString().toInt(), hoursEntry.text.toString().toFloat(), user.userID)
                            }


                        }
                    }.orientation = LinearLayout.VERTICAL

                    cancelButton { }

                }

            }.show()


        }


    }

    private fun addDate(timeItem: TimeItem, date: Int, hours: Float, userID: Int?) {

        if (timeItem.dates!!.keys.contains(date)) {
            toast("Selected date already has a time entry.")
        } else {

            var monthsArray = (resources.getStringArray(R.array.months_array))
            var calendar: Calendar = Calendar.getInstance()
            calendar.set(timeItem.year!!.toInt(), monthsArray.indexOf(timeItem.month), date)

            if (calendar.get(Calendar.MONTH) != monthsArray.indexOf(timeItem.month)) {
                toast("Selected date is not a valid date within this month")
            } else {

                timeItem.addNewDate(date, hours)

                val api = PutTime()
                val success = api.putTime(timeItem, userID!!)

                if (success) {
                    val db = DatabaseHelper(this)
                    val tt = TimeTable(db)
                    tt.updateTimeItem(timeItem)
                    recreate()
                } else {

                    alert("Server Communication failed, please check internet connection or contact an administrator. This update has not been saved.") {
                        timeItem.removeDate(date)
                        yesButton {
                            recreate()
                        }
                    }.show()
                }
            }
        }
    }


    private fun getDates(timeItem: TimeItem, userID: Int?) {


        var timeDates: List<Pair<Int, Float>> = timeItem.dates!!.toList()
        warn("time retrieved: " + timeDates)

        dateDetailsAdapter.timeItem = timeItem
        dateDetailsAdapter.activity = this
        dateDetailsAdapter.userID = userID
        dateDetailsAdapter.dateItems.clear()
        dateDetailsAdapter.dateItems.addAll(timeDates)
        dateDetailsAdapter.notifyDataSetChanged()

    }

}


