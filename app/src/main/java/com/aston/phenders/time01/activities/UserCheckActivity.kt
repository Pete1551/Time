package com.aston.phenders.time01.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.aston.phenders.time01.database.DatabaseHelper
import com.aston.phenders.time01.database.UserTable
import com.aston.phenders.time01.models.User
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn

class UserCheckActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //No UI, could add splash screen but currently not enough processing to warrant it
        checkRegistration()

    }


    fun checkRegistration() {

        val db = DatabaseHelper(this)
        val ut = UserTable(db)

        val user: User = ut.getUser()
        warn("check reg user id = " + user.userID)
        if (user.userID == 0) {
            //user has not previously registered - 0 is default value
            var registrationPageIntent = Intent(this, RegistrationActivity::class.java)
            this.startActivity(registrationPageIntent)
        } else {
            var mainPageIntent = Intent(this, MainActivity::class.java)
            this.startActivity(mainPageIntent)
        }
    }
}
