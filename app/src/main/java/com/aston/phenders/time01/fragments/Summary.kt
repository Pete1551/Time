package com.aston.phenders.time01.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.aston.phenders.time01.R
import com.aston.phenders.time01.adapters.TimeCardRecyclerAdapter
import com.aston.phenders.time01.database.DatabaseHelper
import com.aston.phenders.time01.database.TimeTable
import org.jetbrains.anko.AnkoLogger


class Summary : Fragment(), AnkoLogger {

    var summaryRecycler: RecyclerView? = null
    var timeCardAdapter: TimeCardRecyclerAdapter = TimeCardRecyclerAdapter()
    var monthSpinner: Spinner? = null
    var yearSpinner: Spinner? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_summary, container, false)

        summaryRecycler = view.findViewById(R.id.summary_recycler)
        summaryRecycler!!.layoutManager = LinearLayoutManager(context)
        summaryRecycler!!.adapter = timeCardAdapter

        monthSpinner = view.findViewById(R.id.month_spinner)
        val adapter = ArrayAdapter.createFromResource(context,
                R.array.months_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthSpinner!!.adapter = adapter

        yearSpinner = view.findViewById(R.id.year_spinner) as Spinner
        val yearSpinnerAdapter = ArrayAdapter.createFromResource(context,
                R.array.years_array, android.R.layout.simple_spinner_item)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        yearSpinner!!.adapter = yearSpinnerAdapter


        monthSpinner!!.setSelection(7)//change
        yearSpinner!!.setSelection(0)//change


        return view
    }

    override fun onResume() {
        super.onResume()

        var db = DatabaseHelper(activity!!.applicationContext)

        var month: String = monthSpinner!!.selectedItemPosition.toString()

        var year: String = yearSpinner!!.selectedItem.toString()

        val timeBookings = TimeTable(db).getAllTime(month, year)

        timeCardAdapter.currentResults.clear()

        timeCardAdapter.currentResults.addAll(timeBookings)
        timeCardAdapter.notifyDataSetChanged()

    }

    fun getTodaysMonthAsInt() {

        TODO("Not Implemented")
    }

    fun getTodaysYearAsInt() {

        TODO("Not Implemented")
    }
}