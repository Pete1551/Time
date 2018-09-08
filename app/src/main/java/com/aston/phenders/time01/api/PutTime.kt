package com.aston.phenders.time01.api

import com.aston.phenders.time01.models.TimeItem
import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.result.Result
import com.google.gson.Gson
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn

class PutTime : AnkoLogger {


    //trigger on all crud of time item
    //convert time item into body
    //send it

    fun putTime(timeItem: TimeItem, userID: Int): Boolean {


        FuelManager.instance.baseHeaders = mapOf("userID" to userID.toString())

        val gson = Gson()
        val timeGson = gson.toJson(timeItem)
        var success: Boolean = false


        //indeterminateProgressDialog("This a progress dialog").show()
        val (_, response, result) = Urls.putUrl.httpPut().body(timeGson).timeout(5000).responseString()


        warn("result: " + result)
        warn("response :" + response)


        when (result) {
            is Result.Failure -> {
                val ex = result.getException()
                warn("API Failed: " + ex)
                success = false
            }
            is Result.Success -> {
                warn("API Succeeded")
                success = true
            }

        }
        warn { success }
        return success
    }

}
