package com.aston.phenders.time01.fragments

import android.os.Bundle
import android.os.StrictMode
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.aston.phenders.time01.R
import com.aston.phenders.time01.activities.MainActivity
import com.aston.phenders.time01.adapters.TimeCardRecyclerAdapter
import com.aston.phenders.time01.api.GetTime
import com.aston.phenders.time01.repositories.DatabaseHelper
import com.aston.phenders.time01.repositories.TimeTable
import com.aston.phenders.time01.repositories.UserTable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn
import java.util.*


class SummaryFragment : Fragment(), AnkoLogger {

    var summaryRecycler: RecyclerView? = null
    var timeCardAdapter: TimeCardRecyclerAdapter = TimeCardRecyclerAdapter()
    var monthSpinner: Spinner? = null
    var yearSpinner: Spinner? = null
    var refresher: SwipeRefreshLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_summary, container, false)

        val dateNow: Calendar = Calendar.getInstance()

        summaryRecycler = view.findViewById(R.id.summary_recycler)
        summaryRecycler!!.layoutManager = LinearLayoutManager(context)
        summaryRecycler!!.adapter = timeCardAdapter

        refresher = view.findViewById(R.id.refresher)

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

        monthSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                getTimeCards()
            }

        }
        yearSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                getTimeCards()
            }

        }

        monthSpinner!!.setSelection(dateNow.get(Calendar.MONTH))
        yearSpinner!!.setSelection(yearSpinnerAdapter.getPosition((dateNow.get(Calendar.YEAR)).toString()))

        refresher?.setOnRefreshListener {
            refresher?.isRefreshing = true
            updateTimeFromServer()
            getTimeCards()
            refresher?.isRefreshing = false
        }

        updateTimeFromServer()



        return view
    }

    override fun onResume() {
        super.onResume()
        getTimeCards()
    }

    private fun getTimeCards() {
        var db = DatabaseHelper(activity!!.applicationContext)

        var month: String = monthSpinner!!.selectedItem.toString()

        var year: String = yearSpinner!!.selectedItem.toString()

        val timeBookings = TimeTable(db).getAllTime(month, year)

        timeCardAdapter.currentResults.clear()

        timeCardAdapter.currentResults.addAll(timeBookings)
        timeCardAdapter.notifyDataSetChanged()
    }

    private fun updateTimeFromServer() {
        //Allow thread blocking for HTTP Request, required to stop conflicts
        val policy = StrictMode.ThreadPolicy.Builder().permitNetwork().build()
        StrictMode.setThreadPolicy(policy)

        //Get time items from server
        var db = DatabaseHelper(activity!!.applicationContext)
        val userTable = UserTable(db)
        val user = userTable.getUser()
        val api = GetTime()
        val getResponse = api.getServerTime(user.userID!!)

        if (getResponse.success) {

            //get array of existing time ID's
            val timeTable = TimeTable(db)
            val idArrays: ArrayList<Int> = timeTable.getAllTimeIDs()
            (activity as MainActivity).serverItemsDownloaded = true

            if (!getResponse.timeItems.isEmpty()) {

                for (i in getResponse.timeItems) {
                    //insert or update all time items

                    if (idArrays.contains(i.timeID!!.toInt())) {

                        warn { "Already Exists, Updating Time Item: " + i.timeID }
                        timeTable.updateTimeItem(i)
                        idArrays.remove(i.timeID!!.toInt())
                    } else {
                        warn { "Does Not Exist, Creating TimeItem: " + i.timeID }
                        timeTable.addNewTime(i)
                        idArrays.remove(i.timeID!!.toInt())
                    }

                }

            } else warn { "No Items on server" }
            for (i in idArrays) {
                //delete all items not on server
                timeTable.deleteTimeItem(i.toLong())
            }
        } else (activity as MainActivity).serverItemsDownloaded = false
    }
}

