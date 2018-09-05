package com.aston.phenders.time01.API

import com.aston.phenders.time01.models.TimeItem
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelManager
import com.google.gson.Gson
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn

class putTime : AnkoLogger {


    //trigger on all crud of time item
    //convert time item into body
    //send it
    //validate response

    fun putTime(timeItem: TimeItem, userID: Int) {

        FuelManager.instance.baseHeaders = mapOf("userID" to userID.toString())

        val gson = Gson()
        val timeGson = gson.toJson(timeItem)

        Fuel.post(Urls.putUrl).body(timeGson).response { request, response, result ->


            warn("result: " + result)
            warn("response :" + response)

        }
    }
}