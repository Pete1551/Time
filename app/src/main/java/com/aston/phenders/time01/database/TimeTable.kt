package com.aston.phenders.time01.database

import com.aston.phenders.time01.models.TimeItem
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.db.*
import org.jetbrains.anko.warn

class TimeTable(val db: DatabaseHelper) : AnkoLogger {
    val gson = Gson()

    fun getAllTime(monthFilter: String, yearFilter: String): ArrayList<TimeItem> {

        var bookings: ArrayList<TimeItem> = ArrayList()
        var timeParser = rowParser { timeId: Long?,
                                     month: String?,
                                     startDate: Long?,
                                     endDate: Long?,
                                     year: String?,
                                     dates: String?,
                                     category: String?,
                                     projectCode: String?,
                                     projectTask: String?,
                                     quantity: Float ->
            var time = TimeItem()
            time.timeID = timeId
            time.month = month
            time.startDate = startDate
            time.year = year
            time.endDate = endDate

            time.dates = gson.fromJson<LinkedHashMap<Int, Float>>(dates,
                    object : TypeToken<LinkedHashMap<Int, Float>>() {}.type) //Convert from JSON to ArrayList

            time.category = category
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

    fun getSingleTimeItem(selectedTimeID: Long): TimeItem {
        warn("Selecting Item: " + selectedTimeID)
        var time = TimeItem()
        var timeSingleParser = rowParser { timeId: Long?,
                                           month: String?,
                                           startDate: Long?,
                                           endDate: Long?,
                                           year: String?,
                                           dates: String?,
                                           category: String?,
                                           projectCode: String?,
                                           projectTask: String?,
                                           quantity: Float ->

            time.timeID = timeId
            time.month = month
            time.startDate = startDate
            time.year = year
            time.endDate = endDate

            time.dates = gson.fromJson<LinkedHashMap<Int, Float>>(dates,
                    object : TypeToken<LinkedHashMap<Int, Float>>() {}.type) //Convert from JSON to ArrayList

            time.category = category
            time.projectCode = projectCode
            time.projectTask = projectTask
            time.quantity = quantity
        }
        db.use {
            select("time").whereArgs("(timeId = {id})", "id" to selectedTimeID)
                    .parseOpt(timeSingleParser)
        }

        return time
    }

    fun getLastID(): Long {
        var timeID: Long? = null


        var lastIDParser = rowParser { timeId: Long? ->

            timeID = timeId!!
        }
        db.use {
            select("time").column("MAX(timeId)")

                    .parseOpt(lastIDParser)
        }
        return timeID!!
    }

    fun addNewTime(time: TimeItem) {

        db.use {


            var datesJson = gson.toJson(time.dates) // convert to JSON

            insert("time",

                    "month" to time.month,
                    "year" to time.year,
                    "startDate" to time.startDate,
                    "endDate" to time.endDate,
                    "dates" to datesJson,
                    "category" to time.category,
                    "projectCode" to time.projectCode,
                    "projectTask" to time.projectTask,
                    "quantity" to time.quantity
            )
        }


    }

    fun updateTimeItem(time: TimeItem) {

        val datesJson = gson.toJson(time.dates) // convert to JSON

        db.use {
            update("time",
                    "month" to time.month,
                    "year" to time.year,
                    "startDate" to time.startDate,
                    "endDate" to time.endDate,
                    "dates" to datesJson,
                    "category" to time.category,
                    "projectCode" to time.projectCode,
                    "projectTask" to time.projectTask,
                    "quantity" to time.quantity
            ).whereArgs("(timeId = {id})", "id" to time.timeID!!).exec()
        }
    }

    fun deleteTimeItem(timeID: Long) {

        db.use {

            delete("time", "timeId = {timeID}", "timeID" to timeID)
        }
    }

    fun getAllTimeIDs(): ArrayList<Int> {
        val gson = Gson()
        var timeIDs: ArrayList<Int> = ArrayList()
        var timeIDParser = rowParser { timeId: Long?,
                                       month: String?,
                                       startDate: Long?,
                                       endDate: Long?,
                                       year: String?,
                                       dates: String?,
                                       category: String?,
                                       projectCode: String?,
                                       projectTask: String?,
                                       quantity: Float ->
            var time = TimeItem()
            time.timeID = timeId
            time.month = month
            time.startDate = startDate
            time.year = year
            time.endDate = endDate

            time.dates = gson.fromJson<LinkedHashMap<Int, Float>>(dates,
                    object : TypeToken<LinkedHashMap<Int, Float>>() {}.type) //Convert from JSON to ArrayList

            time.category = category
            time.projectCode = projectCode
            time.projectTask = projectTask
            time.quantity = quantity
            timeIDs.add(timeId!!.toInt())

            //timeIDs.add(timeId!!.toInt())

        }

        db.use {
            select("Time")

                    .parseList(timeIDParser)
        }
        return timeIDs
    }
}


/* fun getAllTimeIDs(): ArrayList<Long> {
     var timeIDs = ArrayList<Long>()


     var lastIDParser = rowParser { timeId: Long? ->

         timeIDs.add(timeId!!)
     }
     db.use {
         select("time").column("timeId")

                 .parse(lastIDParser)
     }
     return timeIDs
 } */
