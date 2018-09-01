package com.aston.phenders.time01.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.aston.phenders.time01.R
import com.aston.phenders.time01.adapters.TimeCardDetailAdapter
import com.aston.phenders.time01.database.DatabaseHelper
import com.aston.phenders.time01.database.TimeTable
import org.jetbrains.anko.AnkoLogger
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

        getDates()
    }

    private fun getDates() {
        var db = DatabaseHelper(applicationContext)
        warn("Getting TimeItem")
        val timeItem = TimeTable(db).getSingleTimeItem(intent.getLongExtra("timeID", -1))

        var timeDates : List<Pair<Int, Float>> = timeItem.dates!!.toList()
        warn("time retrieved: " + timeDates)

        dateDetailsAdapter.dateItems.clear()
        dateDetailsAdapter.dateItems.addAll(timeDates)
        dateDetailsAdapter.month = timeItem.month!!
        dateDetailsAdapter.notifyDataSetChanged()
    }

}


