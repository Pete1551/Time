package com.aston.phenders.time01.API

import com.github.kittinunf.fuel.Fuel

class putTime {



    //trigger on all crud of time item
    //convert time item into body
    //send it
    //validate response

    fun putTime(){



        Fuel.post("http://httpbin.org/post").body("{ \"foo\" : \"bar\" }").response { request, response, result ->

            val r = response
        }

    }


}