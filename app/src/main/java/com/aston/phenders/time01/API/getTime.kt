package com.aston.phenders.time01.API

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn

class getTime : AnkoLogger {

    fun getMissingTime(userID : Int, year: String, dates: ArrayList<Int>) {


        FuelManager.instance.baseHeaders = mapOf("UserID" to 1.toString(), "dates" to dates.toString())


        Urls.getUrl.httpPost().responseString { request, response, result ->
            //do something with response

            warn("request: " + request)
            when (result) {
                is Result.Failure -> {
                    val ex = result.getException()
                    warn("API Fail?")
                    warn("result: " + result)
                }
                is Result.Success -> {
                    val data = result.get()
                    warn("API Success?")
                    warn("Response: " + response)

                }
            }
        }
    }
}