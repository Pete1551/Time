package com.aston.phenders.time01.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.aston.phenders.time01.R
import org.jetbrains.anko.AnkoLogger

class Profile : Fragment(), AnkoLogger {
    //var text: TextView? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        var view = inflater.inflate(R.layout.fragment_profile, container, false)
        val text = view.findViewById<TextView>(R.id.textView2)



        return view
    }

}