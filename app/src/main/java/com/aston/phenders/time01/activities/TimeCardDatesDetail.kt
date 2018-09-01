package com.aston.phenders.time01.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.aston.phenders.time01.R
import com.aston.phenders.time01.adapters.TimeCardDetailAdapter
import com.aston.phenders.time01.database.DatabaseHelper
import com.aston.phenders.time01.database.TimeTable
import com.aston.phenders.time01.models.TimeItem
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.UI
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.warn

class TimeCardDatesDetail : AppCompatActivity(), AnkoLogger {



    private var detailsView: RecyclerView? = null
    private var dateDetailsAdapter = TimeCardDetailAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        warn("Create Time Card Details Activity")
        setContentView(R.layout.activity_card_dates_detail)

        detailsView = findViewById(R.id.time_detail_recycler)
        detailsView!!.layoutManager = LinearLayoutManager(this)
        detailsView!!.adapter = dateDetailsAdapter

        val projectCodeTask: TextView = findViewById(R.id.detail_project_code_task)
        val timeCategoory: TextView = findViewById(R.id.detail_category)
        val datePeriod: TextView = findViewById(R.id.detail_date_period)
        val quantity: TextView = findViewById(R.id.detail_quantity)


        doAsync {
            var db = DatabaseHelper(applicationContext)
            warn("Getting TimeItem")
            val timeItem = TimeTable(db).getSingleTimeItem(intent.getLongExtra("timeID", -1))

            getDates(timeItem)

            UI {
                projectCodeTask.text = "Code: " + timeItem.projectCode + " Task: " + timeItem.projectTask
                timeCategoory.text = timeItem.category
                datePeriod.text =
                        "Period: " + timeItem.startDate.toString() + "/" + timeItem.month + " -> " + timeItem.endDate.toString() + "/" + timeItem.month
                quantity.text = timeItem.quantity.toString() + " Hours"

            }
        }

    }

    private fun getDates(timeItem: TimeItem) {


    var timeDates: List<Pair<Int, Float>> = timeItem.dates!!.toList()
    warn("time retrieved: " + timeDates)

    dateDetailsAdapter.dateItems.clear()
    dateDetailsAdapter.dateItems.addAll(timeDates)
    dateDetailsAdapter.month = timeItem.month!!
    dateDetailsAdapter.notifyDataSetChanged()

    }

}


