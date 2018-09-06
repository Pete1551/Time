package com.aston.phenders.time01.models

class TimeItem {
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
        this.dates?.putAll(this.dates!!.toSortedMap())
        this.quantity += hours

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

    fun removeDate(date: Int) {

        this.quantity = this.quantity - this.dates!!.getValue(date)
        this.dates?.remove(date)

    }

}



