package com.aston.phenders.time01.database

import com.aston.phenders.time01.models.TimeItem
import com.google.gson.Gson
import org.jetbrains.anko.db.insert
import org.jetbrains.anko.db.rowParser
import org.jetbrains.anko.db.select

class TimeTable(val db: DatabaseHelper) {

    fun getAllTime(monthFilter: String, yearFilter: String): ArrayList<TimeItem> {

        var bookings: ArrayList<TimeItem> = ArrayList()
        var timeParser = rowParser { timeId: Long?,
                                     month: String?,
                                     startDate: Long?,
                                     endDate: Long?,
                                     year: String?,
                                     dates: String?,
                                     category: String?,
                                     businessReason: String?,
                                     projectCode: String?,
                                     projectTask: String?,
                                     quantity: Float ->
            var time = TimeItem()
            time.timeId = timeId
            time.month = month
            time.startDate = startDate
            time.year = year
            time.endDate = endDate
            time.dates = dates
            time.category = category
            time.businessReason = businessReason
            time.projectCode = projectCode
            time.projectTask = projectTask
            time.quantity = quantity
            bookings.add(time)
        }
        db.use {
            select("Time")
                    .whereArgs("(month = {month}) and (year = {year})",
                            "month" to monthFilter,
                            "year" to yearFilter)
                    .parseList(timeParser)
        }
        return bookings
    }

    fun addNewTime(time: TimeItem) {




//datesJson = gson.toJson(response)




        db.use {
            insert("time",

                    "month" to time.month,
                    "year" to time.year,
                    "startDate" to time.startDate,
                    "endDate" to time.endDate,
                    "dates" to time.dates,
                    "category" to time.category,
                    "businessReason" to time.businessReason,
                    "projectCode" to time.projectCode,
                    "projectTask" to time.projectTask,
                    "quantity" to time.quantity
            )
        }


    }

    fun CalculateQuantity() {

        //Currently handled in BookTime.kt

        TODO()
    }
}

