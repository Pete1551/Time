package com.aston.phenders.time01.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.aston.phenders.time01.R
import com.aston.phenders.time01.fragments.BookTime
import com.aston.phenders.time01.fragments.Profile
import com.aston.phenders.time01.fragments.Summary
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    private val profile: Profile = Profile()
    val summary: Summary
    private val bookTime: BookTime
    var loadPrefs : Boolean = true

    init {

        summary = Summary()
        bookTime = BookTime()
    }


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        val transaction = supportFragmentManager.beginTransaction()

        when (item.itemId) {
            R.id.navigation_summary -> {
                transaction.replace(R.id.main_fragment, summary)
            }

            R.id.navigation_profile -> {
                transaction.replace(R.id.main_fragment, profile)
            }
            R.id.navigation_book_time -> {
                transaction.replace(R.id.main_fragment, bookTime)
            }
        }
        transaction.commit()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.main_fragment, summary)
        transaction.commit()

    }

    fun returnToSummary() {
        loadPrefs = true
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_fragment, summary)
        transaction.commit()
    }
}

