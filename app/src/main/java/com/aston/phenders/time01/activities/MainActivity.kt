package com.aston.phenders.time01.activities

import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v7.app.AppCompatActivity
import com.aston.phenders.time01.R
import com.aston.phenders.time01.fragments.BookTimeFragment
import com.aston.phenders.time01.fragments.ProfileFragment
import com.aston.phenders.time01.fragments.SummaryFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.yesButton


class MainActivity : AppCompatActivity(), AnkoLogger {

    private val profileFragment: ProfileFragment = ProfileFragment()
    private val summaryFragment: SummaryFragment = SummaryFragment()
    private var bookTimeFragment: BookTimeFragment = BookTimeFragment()
    private var bottomNavigationView: BottomNavigationView? = null

    var loadPrefs: Boolean = true
    var serverItemsDownloaded: Boolean = false


    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->

        val transaction = supportFragmentManager.beginTransaction()

        when (item.itemId) {
            R.id.navigation_summary -> {
                transaction.replace(R.id.main_fragment, summaryFragment)
            }

            R.id.navigation_profile -> {
                transaction.replace(R.id.main_fragment, profileFragment)
            }
            R.id.navigation_book_time -> {
                if (serverItemsDownloaded)
                    transaction.replace(R.id.main_fragment, bookTimeFragment)
                else {
                    alert("BookTime is disabled until a connection can be established with the server. Refresh the summary view to try again.") {
                        yesButton { bottomNavigationView!!.selectedItemId = R.id.navigation_summary }
                    }.show()
                }
            }
        }
        transaction.commit()
        true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        //set up bottom navigation bar and set summary as first page.
        bottomNavigationView = findViewById(R.id.navigation)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.main_fragment, summaryFragment)
        transaction.commit()


    }

    fun returnToSummary() {
        loadPrefs = true
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.main_fragment, summaryFragment)
        transaction.commit()
        bottomNavigationView!!.selectedItemId = R.id.navigation_summary

    }


}



