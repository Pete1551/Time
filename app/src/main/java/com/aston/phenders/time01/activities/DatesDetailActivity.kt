package com.aston.phenders.time01.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import com.aston.phenders.time01.R
import com.aston.phenders.time01.adapters.DatesDetailCardAdapter
import com.aston.phenders.time01.database.DatabaseHelper
import com.aston.phenders.time01.database.TimeTable
import com.aston.phenders.time01.models.TimeItem
import org.jetbrains.anko.*

class DatesDetailActivity : AppCompatActivity(),  AnkoLogger {


    private var detailsView: RecyclerView? = null
    private var dateDetailsAdapter = DatesDetailCardAdapter()

    var projectCodeTask: TextView? = null
    var timeCategory: TextView? = null
    var datePeriod: TextView? = null
    var quantity: TextView? = null
    var backButton: ImageView? = null
    var deleteButton: ImageView? = null

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


    }

    override fun onResume() {
        super.onResume()

        var db = DatabaseHelper(applicationContext)
        warn("Getting TimeItem")
        val timeItem = TimeTable(db).getSingleTimeItem(intent.getLongExtra("timeID", -1))

        getDates(timeItem)



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
                    warn(" deleting timeID: " + timeItem.timeId)
                    val tt = TimeTable(db)
                    tt.deleteTimeItem(timeItem.timeId!!)
                    finish()
                }
                noButton {

                }
            }.show()


        }
    }


    private fun getDates(timeItem: TimeItem) {



        var timeDates: List<Pair<Int, Float>> = timeItem.dates!!.toList()
        warn("time retrieved: " + timeDates)

        dateDetailsAdapter.timeItem = timeItem
        dateDetailsAdapter.activity = this
        dateDetailsAdapter.dateItems.clear()
        dateDetailsAdapter.dateItems.addAll(timeDates)
        dateDetailsAdapter.notifyDataSetChanged()

    }

}


