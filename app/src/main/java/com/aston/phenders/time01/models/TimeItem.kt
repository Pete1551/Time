package com.aston.phenders.time01.models

import org.jetbrains.anko.AnkoLogger

class TimeItem : AnkoLogger {
    var timeID: Long? = null
    var month: String? = null
    var startDate: Long? = null
    var endDate: Long? = null
    var year: String? = null
    var dates: LinkedHashMap<Int, Float>? = null
    var category: String? = null
    var projectCode: String? = null
    var projectTask: String? = null
    var quantity: Float = 0F

    fun addNewDate(date: Int, hours: Float) {

        this.dates?.put(date, hours)

        // Sort Dates
        val tempMap = this.dates!!.toSortedMap()
        this.dates!!.clear()
        for (i in tempMap) {
            this.dates?.put(i.key, i.value)
        }
        this.quantity += hours

        //replace start and date if required
        if (this.startDate!! >= date)
            this.startDate = date.toLong()
        else if (this.endDate!! <= date)
            this.endDate = date.toLong()
    }

    fun updateDateHours(date: Int, hours: Float) {

        this.quantity = this.quantity - this.dates!!.getValue(date)
        this.quantity = this.quantity + hours
        this.dates?.put(date, hours)
        this.quantity + hours

    }

    fun removeDate(date: Int): String {
        var msg = "Deleted"

        this.quantity = this.quantity - this.dates!!.getValue(date)
        this.dates?.remove(date)

        if (this.dates!!.isEmpty()) {
            this.dates!!.put(date, 0F)
            msg = "Cannot delete the last date item"
        }
        this.startDate = this.dates!!.keys.first().toLong()
        this.endDate = this.dates!!.keys.last().toLong()
        return msg
    }

}



