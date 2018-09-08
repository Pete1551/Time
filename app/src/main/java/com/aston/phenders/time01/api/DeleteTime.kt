package com.aston.phenders.time01.api

import com.github.kittinunf.fuel.core.FuelManager
import com.github.kittinunf.fuel.httpDelete
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.fuel.httpPut
import com.github.kittinunf.result.Result
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn

class DeleteTime: AnkoLogger {

    fun deleteTimeItem(userID : Int, timeID : Int) : Boolean{

        FuelManager.instance.baseHeaders = mapOf("userID" to userID.toString(), "timeID" to timeID.toString())
        var success: Boolean = false

        val (_, response, result) = Urls.deleteUrl.httpGet().timeout(5000).responseString()

        when (result) {
            is Result.Failure -> {
                val ex = result.getException()
                warn("Delete API Failed: " + ex)
                success = false
            }
            is Result.Success -> {
                warn("Delete API Succeeded")
                success = true
            }

        }

        return success
    }

}
