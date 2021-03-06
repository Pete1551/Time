package com.aston.phenders.time01.api

import com.aston.phenders.time01.models.APIGetResponse
import com.aston.phenders.time01.models.TimeItem
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn


class GetTime : AnkoLogger {

    fun getServerTime(userID: Int): APIGetResponse {

        var getResponse = APIGetResponse()
        var timeItems = ArrayList<TimeItem>()

        FuelManager.instance.baseHeaders = mapOf("userID" to userID.toString())

        val (_, _, result) = Urls.getUrl.httpGet().timeout(5000).responseString()

        //do something with response
        when (result) {
            is Result.Failure -> {
                val ex = result.getException()
                warn("API Failed")
                warn("result: " + result)
                getResponse.success = false
            }
            is Result.Success -> {

                warn("API Success")
                val body = result.get().trim()
                warn { body }
                //Json Parser to retrieve time objects from result
                timeItems = Gson().fromJson<ArrayList<TimeItem>>(body,
                        object : TypeToken<ArrayList<TimeItem>>() {}.type)

                getResponse.success = true
                getResponse.timeItems = timeItems
            }
        }
        return getResponse
    }
}



