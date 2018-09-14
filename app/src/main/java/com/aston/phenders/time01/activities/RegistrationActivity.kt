package com.aston.phenders.time01.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.EditText
import com.aston.phenders.time01.R
import com.aston.phenders.time01.repositories.DatabaseHelper
import com.aston.phenders.time01.repositories.UserTable


class RegistrationActivity : AppCompatActivity() {

    lateinit var userIDInput: EditText
    lateinit var submitButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        // Set up the login form.

        userIDInput = findViewById(R.id.userID)
        submitButton = findViewById(R.id.register_button)


        submitButton.setOnClickListener { _: View? ->

            //get user object and set ID
            val db = DatabaseHelper(this)
            val ut = UserTable(db)
            val user = ut.getUser()
            user.userID = userIDInput.text.toString().toInt()
            ut.updateUser(user)

            //return to main page
            var mainPageIntent = Intent(this, MainActivity::class.java)
            this.startActivity(mainPageIntent)
        }


    }


}
