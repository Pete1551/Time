package com.aston.phenders.time01.api

import com.aston.phenders.time01.models.TimeItem
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn


class GetTime : AnkoLogger {

    fun getServerTime(userID: Int) {

        FuelManager.instance.baseHeaders = mapOf("userID" to userID.toString())

        Urls.getUrl.httpGet().responseString { _, _, result ->

            //do something with response
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    warn("API Failed")
                    warn("result: " + result)
                }
                is Result.Success -> {

                    warn("API Success")
                    val body = result.get().trim()

                    //Json Parser to retrieve time objects
                    var timeItems = Gson().fromJson<ArrayList<TimeItem>>(body,
                            object : TypeToken<ArrayList<TimeItem>>() {}.type)
                    for (i in timeItems) {
                        warn { i }
                        warn(i.timeID)
                        warn { i.month }
                        warn { i.startDate }
                        warn { i.endDate }
                        warn { i.year }
                        warn { i.dates }
                        warn(i.category)
                        warn(i.projectCode)
                        warn(i.projectTask)
                        warn { i.quantity }

                    }
                }
            }
        }
    }
}

