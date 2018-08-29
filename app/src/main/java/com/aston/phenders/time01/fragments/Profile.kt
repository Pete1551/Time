package com.aston.phenders.time01.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aston.phenders.time01.R
import com.aston.phenders.time01.database.DatabaseHelper
import com.aston.phenders.time01.database.UserTable
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn

class Profile : Fragment(), AnkoLogger {
    //var text: TextView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_profile, container, false)



        val db = DatabaseHelper(activity!!.applicationContext)
        val userTable = UserTable(db)

        var user = userTable.getUser()

        warn("Test User : " + user.userID)
        warn(user)
        warn(user.projectCode)

        return view
    }

    fun getUser() {

    }

    fun updateUser() {

    }

}